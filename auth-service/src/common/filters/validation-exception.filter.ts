import { ExceptionFilter, Catch, ArgumentsHost, HttpStatus } from '@nestjs/common';
import { Response } from 'express';
import { ValidationError } from 'class-validator';

@Catch(ValidationError)
export class ValidationExceptionFilter implements ExceptionFilter {
    catch(exception: ValidationError, host: ArgumentsHost) {
        const ctx = host.switchToHttp();
        const response = ctx.getResponse<Response>();

        const errors = this.formatErrors(exception);

        response.status(HttpStatus.BAD_REQUEST).json({
            statusCode: HttpStatus.BAD_REQUEST,
            error: 'ValidationException',
            message: 'Validation failed',
            errors,
            timestamp: new Date().toISOString(),
            path: ctx.getRequest().url,
        });
    }

    private formatErrors(exception: ValidationError): any {
        const errors = {};

        const constraints = exception.constraints || {};
        if (Object.keys(constraints).length > 0) {
            const messages = Object.values(constraints).flat();
            errors[exception.property] = messages;
        }

        if (exception.children && exception.children.length > 0) {
            exception.children.forEach(child => {
                const childErrors = this.formatErrors(child);
                Object.assign(errors, childErrors);
            });
        }

        return errors;
    }
}