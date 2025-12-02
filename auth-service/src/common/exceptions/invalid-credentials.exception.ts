import { HttpStatus } from "@nestjs/common";
import { BusinessException } from "./business.exception";

export class InvalidCredentialsException extends BusinessException {
    constructor() {
        super('Invalid credentials', HttpStatus.UNAUTHORIZED);
        this.name = 'InvalidCredentialsException';
    }
}