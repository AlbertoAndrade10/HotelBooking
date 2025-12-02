import { Controller, Post, Body, UseGuards, Req, Res, Patch, Param } from '@nestjs/common';
import express from 'express';
import { AuthService } from './auth.service';
import { RegisterDto } from './dto/register.dto';
import { LoginDto } from './dto/login.dto';
import { JwtAuthGuard } from './guards/accessToken.guard';
import { ChangePasswordDto } from './dto/change-password.dto';
import { RefreshTokenGuard } from './guards/refreshToken.guard';
import { RolesGuard } from './guards/roles.guard';
import { Roles } from './decorators/role.decorator';
import { UpdateRoleDto } from './dto/updated-role.dto';


@Controller('auth')
export class AuthController {
  constructor(private readonly authService: AuthService) { }


  /* 
  REGISTER
  @desc Creates a new user
  @body RegisterDto (email, password)
  @return success message
  */
  @Post('register')
  async register(@Body() registerDto: RegisterDto) {
    return await this.authService.register(registerDto);
  }

  /*   
  LOGIN
  @desc Authenticates user and issues tokens
  @body LoginDto (email, password)
  @return accessToken (response)
  @return refreshToken (cookie) */
  @Post('login')
  async login(@Body() loginDto: LoginDto, @Res({ passthrough: true }) res: express.Response) {
    const result = await this.authService.login(loginDto);
    res.cookie('refreshToken', result.refreshToken, {
      httpOnly: true,
      secure: false,
      maxAge: 7 * 24 * 60 * 60 * 1000,
    });
    return result;
  }

  /*
  LOGOUT
  @desc Invalidates user's refresh token and clears cookie
  @return success message 
  */
  @Post('logout')
  async logout(@Req() req: express.Request, @Res({ passthrough: true }) res: express.Response) {

    const refreshToken = req.cookies?.refreshToken;

    if (refreshToken) {
      await this.authService.logout(refreshToken);
    }

    res.clearCookie('refreshToken');

    return { message: 'Logged out successfully' };
  }

  /*
  REFRESH TOKENS
  @desc Issues new access and refresh tokens
  @guard RefreshTokenGuard (extracts validated refresh token and user)
  @return accessToken (response)
  @return refreshToken (cookie)
  */
  @Post('refresh')
  @UseGuards(RefreshTokenGuard)
  async refresh(
    @Req() req,
    @Res({ passthrough: true }) res: express.Response
  ) {

    const oldRefreshToken = req.cookies.refreshToken;

    const { accessToken, refreshToken } =
      await this.authService.refreshTokens(oldRefreshToken);

    res.cookie('refreshToken', refreshToken, {
      httpOnly: true,
      secure: false,
      maxAge: 7 * 24 * 60 * 60 * 1000,
    });

    return { accessToken, refreshToken };
  }




  /*
  CHANGE PASSWORD
  @desc Changes the password of the authenticated user
  @guard JwtAuthGuard
  @body ChangePasswordDto (currentPassword, newPassword)
  @return success message 
  */
  @Patch('change-password')
  @UseGuards(JwtAuthGuard)
  async changePassword(@Req() req, @Body() dto: ChangePasswordDto) {
    return this.authService.changePassword(req.user.sub, dto);
  }
  // ADMIN: change user role
  @Patch('users/:id/role')
  @UseGuards(JwtAuthGuard, RolesGuard)
  @Roles('ADMIN')
  async updateUserRole(@Param('id') id: string, @Body() dto: UpdateRoleDto) {
    return this.authService.updateUserRole(id, dto.role);
  }

}