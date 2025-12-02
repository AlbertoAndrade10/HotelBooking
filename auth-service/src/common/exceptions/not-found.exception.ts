import { HttpStatus } from '@nestjs/common';
import { BusinessException } from './business.exception';

export class NotFoundException extends BusinessException {
    constructor(entity: string, id?: string | number) {
        const message = id ? `${entity} with ID ${id} not found` : `${entity} not found`;
        super(message, HttpStatus.NOT_FOUND);
        this.name = 'NotFoundException';
    }
}