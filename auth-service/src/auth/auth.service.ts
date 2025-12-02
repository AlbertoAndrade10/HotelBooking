import { Injectable } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { Repository } from 'typeorm';
import { JwtService } from '@nestjs/jwt';
import * as bcrypt from 'bcryptjs';
import { Role, User } from './entities/user.entity';
import { RegisterDto } from './dto/register.dto';
import { RedisService } from './redis/redis.service';
import { LoginDto } from './dto/login.dto';
import { BusinessException } from '../common/exceptions/business.exception';
import { UserNotFoundException } from '../common/exceptions/user-not-found.exception';
import { InvalidCredentialsException } from '../common/exceptions/invalid-credentials.exception';
import { DataBaseException } from '../common/exceptions/database.exception';
import { QueryFailedError } from 'typeorm';
import { KafkaService } from 'src/kafka/kafka.service';
import { UpdateCredentialsDto } from './dto/update-credentials.dto';
import { ChangePasswordDto } from './dto/change-password.dto';

@Injectable()
export class AuthService {
  constructor(
    @InjectRepository(User) private userRepository: Repository<User>,
    private jwtService: JwtService,
    private redisService: RedisService,
    private kafkaService: KafkaService,
  ) { }

  /*   REGISTER
    Creates a new user → hashes password → saves to DB → emits Kafka event */
  async register(registerDto: RegisterDto) {
    const { email, password } = registerDto;

    try {
      const existing = await this.userRepository.findOne({ where: { email } });
      if (existing) {
        throw new BusinessException("Email already exists", 409);
      }

      const hash = await bcrypt.hash(password, 10);

      const authUser = await this.userRepository.save(
        this.userRepository.create({
          email,
          password: hash,
          role: Role.USER, // explicit default role
        })
      );

      // Emit event to UserService
      await this.kafkaService.sendUserRegisteredEvent({
        id: authUser.id,
        email: authUser.email,
        role: authUser.role

      });

      return { id: authUser.id, message: "User registered successfully" };
    } catch (error) {
      if (error instanceof BusinessException) throw error;

      if (error instanceof QueryFailedError) {
        throw new DataBaseException(`Database error during register: ${error.message}`);
      }

      throw new DataBaseException("Unknown error during register");
    }
  }

  /* LOGIN
     @return accessToken (returned in response)
     @return refreshToken (stored in Redis and set as cookie)
     Validates credentials, generates tokens, stores refresh token */
  async login(loginDto: LoginDto): Promise<{ accessToken: string; refreshToken: string }> {
    const { email, password } = loginDto;

    try {
      const user = await this.userRepository.findOne({ where: { email } });
      if (!user) throw new UserNotFoundException();

      const valid = await bcrypt.compare(password, user.password);
      if (!valid) throw new InvalidCredentialsException();

      // payloads include the token type
      const accessPayload = { sub: user.id, email: user.email, type: 'access', role: user.role };
      const refreshPayload = { sub: user.id, type: 'refresh' };

      // access short (15min), refresh long (7days)
      const accessToken = this.jwtService.sign(accessPayload, {
        expiresIn: '15m',
        secret: process.env.JWT_ACCESS_SECRET,
      });
      const refreshToken = this.jwtService.sign(refreshPayload, {
        expiresIn: '7d',
        secret: process.env.JWT_REFRESH_SECRET,
      });

      await this.redisService.setex(
        `refresh_token:${user.id}`,
        604800, // 7 days in sec
        refreshToken
      );

      return { accessToken, refreshToken };
    } catch (error) {

      if (error instanceof UserNotFoundException) throw error;
      if (error instanceof InvalidCredentialsException) throw error;

      if (error instanceof QueryFailedError) {
        throw new DataBaseException(`Database error during login: ${error.message}`);
      }

      throw new DataBaseException("Unknown error during login");
    }
  }

  /* LOGOUT
     Deletes user’s refresh token from Redis
     User must log in again to get new tokens
     @param refreshToken (read from cookie)
  */
  async logout(refreshToken: string): Promise<void> {
    try {
      const decoded = this.jwtService.decode(refreshToken) as any;

      if (decoded?.sub) {
        await this.redisService.del(`refresh_token:${decoded.sub}`);
      }
    } catch (error) {
      throw new DataBaseException(`Error during logout: ${error.message}`);
    }
  }

  /* UPDATE CREDENTIALS
     Updates user email and/or password
     Emits Kafka event to User Microservice
     @param id User UUID
     @return confirmation message
  */
  async updateCredentials(id: string, updateDto: UpdateCredentialsDto) {
    const user = await this.userRepository.findOne({
      where: { id }
    });

    if (!user) throw new UserNotFoundException();

    if (updateDto.email) user.email = updateDto.email;
    if (updateDto.password) {
      user.password = await bcrypt.hash(updateDto.password, 10);
    }

    await this.userRepository.save(user);

    // emit event to UserService
    await this.kafkaService.sendUserUpdatedEvent({
      id: user.id,
      email: user.email,
    });

    return { message: "Credentials updated" };
  }


  /* CHANGE PASSWORD
     Checks old password → hashes new password → saves user
     Deletes refresh token in Redis (forces token re-login)
     @param userId UUID of user requesting change
     @return success message
  */
  async changePassword(userId: string, dto: ChangePasswordDto) {
    const user = await this.userRepository.findOne({ where: { id: userId } });

    if (!user) {
      throw new UserNotFoundException();
    }

    const passwordValid = await bcrypt.compare(dto.currentPassword, user.password);

    if (!passwordValid) {
      throw new InvalidCredentialsException();
    }

    user.password = await bcrypt.hash(dto.newPassword, 10);
    await this.userRepository.save(user);

    // Invalidate stored refresh token after password change
    await this.redisService.del(`refresh_token:${user.id}`);

    return { message: "Password updated successfully" };
  }


  /* REFRESH TOKENS
     Validates refresh token → compares with Redis copy
     Generates new access token + new refresh token
     Stores new refresh token in Redis (rotation)
     @return new access & refresh tokens
  */
  async refreshTokens(refreshToken: string) {
    try {
      const decoded = this.jwtService.verify(refreshToken, {
        secret: process.env.JWT_REFRESH_SECRET,
      });


      // verify its refresh token
      if (!decoded || decoded.type !== 'refresh') {
        throw new InvalidCredentialsException();
      }

      const stored = await this.redisService.get(`refresh_token:${decoded.sub}`);
      if (stored !== refreshToken) {
        throw new InvalidCredentialsException();
      }

      const user = await this.userRepository.findOne({ where: { id: decoded.sub } });
      if (!user) throw new UserNotFoundException();

      const accessPayload = { sub: user.id, email: user.email, type: 'access', role: user.role };
      const refreshPayload = { sub: user.id, type: 'refresh' };

      const newAccessToken = this.jwtService.sign(accessPayload, {
        expiresIn: '15m',
        secret: process.env.JWT_ACCESS_SECRET,
      });
      const newRefreshToken = this.jwtService.sign(refreshPayload, {
        expiresIn: '7d',
        secret: process.env.JWT_REFRESH_SECRET,
      });

      await this.redisService.setex(
        `refresh_token:${user.id}`,
        604800,
        newRefreshToken
      );

      return { accessToken: newAccessToken, refreshToken: newRefreshToken };
    } catch (error) {
      if (error instanceof UserNotFoundException) throw error;
      if (error instanceof InvalidCredentialsException) throw error;

      if (error instanceof QueryFailedError) {
        throw new DataBaseException(`Database error during token refresh: ${error.message}`);
      }

      throw new InvalidCredentialsException();
    }
  }


  /*UPDATE USER ROLES (admin only)
   * 
   */
  async updateUserRole(userId: string, newRole: Role) {
    const user = await this.userRepository.findOne({ where: { id: userId } });
    if (!user) throw new UserNotFoundException();

    user.role = newRole;
    await this.userRepository.save(user);

    //  emit an event to user-service so profile can be aware
    await this.kafkaService.sendUserUpdatedEvent({
      id: user.id,
      email: user.email,
      role: user.role,
    });

    return { message: 'Role updated successfully' };
  }
}