import { HttpStatus } from "@nestjs/common";
import { BusinessException } from "./business.exception";

export class ValidationException extends BusinessException {
    constructor(message: string) {
        super(message, HttpStatus.BAD_REQUEST);
        this.name = 'ValidationException';
    }
}