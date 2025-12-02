import { HttpStatus } from "@nestjs/common";
import { BusinessException } from "./business.exception";

export class DataBaseException extends BusinessException {
    constructor(message: string) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR);
        this.name = 'DataBaseException';
    }
}