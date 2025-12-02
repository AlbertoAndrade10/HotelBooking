import { HttpStatus } from "@nestjs/common";
import { BusinessException } from "./business.exception";

export class UserNotFoundException extends BusinessException {
    constructor() {
        super('User not found', HttpStatus.NOT_FOUND);
        this.name = 'UserNotFoundException';
    }
}