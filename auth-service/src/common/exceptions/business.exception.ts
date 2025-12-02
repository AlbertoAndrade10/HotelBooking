import { HttpStatus } from "@nestjs/common";

export class BusinessException extends Error {
    public readonly status: HttpStatus;

    constructor(message: string, status: HttpStatus){
        super(message);
        this.status = status;
        this.name = "BusinessException";
    }
}