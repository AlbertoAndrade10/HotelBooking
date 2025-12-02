import { Module } from '@nestjs/common';
import { TypeOrmModule } from '@nestjs/typeorm';
import { ConfigModule, ConfigService } from '@nestjs/config';


import { AuthModule } from './auth/auth.module';
import { RedisService } from './auth/redis/redis.service';
import { AppController } from './app.controller';
import { AppService } from './app.service';
import { ConsulService } from './consul/consul.service';
import { RolesGuard } from './auth/guards/roles.guard';
import { JwtAuthGuard } from './auth/guards/accessToken.guard';

@Module({
  imports: [
    ConfigModule.forRoot({ isGlobal: true }),
    TypeOrmModule.forRootAsync({
      imports: [ConfigModule],
      useFactory: (configService: ConfigService) => ({
        type: 'postgres',
        host: configService.get('DB_HOST', 'localhost'),
        port: configService.get('DB_PORT', 5432),
        username: configService.get('DB_USERNAME', 'authuser'),
        password: configService.get('DB_PASSWORD', 'authpass'),
        database: configService.get('DB_NAME', 'authdb'),
        entities: [__dirname + '/**/*.entity{.ts,.js}'],
        synchronize: true,
      }),
      inject: [ConfigService],
    }),
    AuthModule,
  ],
  controllers: [AppController],
  providers: [AppService, RedisService, ConsulService, RolesGuard, JwtAuthGuard],
})
export class AppModule { }