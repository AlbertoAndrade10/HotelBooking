import { Injectable, UnauthorizedException } from '@nestjs/common';
import { PassportStrategy } from '@nestjs/passport';
import { ExtractJwt, Strategy, StrategyOptionsWithRequest } from 'passport-jwt';
import { ConfigService } from '@nestjs/config';
import { Request } from 'express';

@Injectable()
export class RefreshTokenStrategy extends PassportStrategy(
    Strategy,
    'jwt-refresh',
) {
    constructor(private configService: ConfigService) {
        const secret = configService.get<string>('JWT_REFRESH_SECRET');

        if (!secret) {
            throw new Error('JWT_REFRESH_SECRET is not defined');
        }

        const options: StrategyOptionsWithRequest = {
            jwtFromRequest: ExtractJwt.fromExtractors([
                RefreshTokenStrategy.extractJWTFromCookie,
            ]),
            ignoreExpiration: false,
            secretOrKey: secret,
            passReqToCallback: true,
        };

        super(options);
    }

    private static extractJWTFromCookie(req: Request): string | null {
        return req.cookies?.refreshToken ?? null;
    }

    async validate(req: Request, payload: any) {
        if (payload.type !== 'refresh') {
            throw new UnauthorizedException('Invalid refresh token');
        }

        const refreshToken = req.cookies['refreshToken'];
        if (!refreshToken) {
            throw new UnauthorizedException('Refresh token missing');
        }

        return {
            ...payload,
            refreshToken,
        };
    }
}
