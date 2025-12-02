import { ExceptionFilter, Catch, ArgumentsHost, HttpStatus } from '@nestjs/common';
import { Response } from 'express';
import { QueryFailedError } from 'typeorm';

@Catch(QueryFailedError)
export class DatabaseExceptionFilter implements ExceptionFilter {
  catch(exception: QueryFailedError, host: ArgumentsHost) {
    const ctx = host.switchToHttp();
    const response = ctx.getResponse<Response>();

    let message = 'Database error occurred';
    let errorType = 'DatabaseException';

    
    if (exception.message.includes('duplicate key value')) {
      message = 'Duplicate entry error';
      errorType = 'DuplicateKeyException';
    } else if (exception.message.includes('violates foreign key constraint')) {
      message = 'Foreign key constraint error';
      errorType = 'ForeignKeyException';
    }

    response.status(HttpStatus.INTERNAL_SERVER_ERROR).json({
      statusCode: HttpStatus.INTERNAL_SERVER_ERROR,
      error: errorType,
      message,
      detail: exception.message,
      timestamp: new Date().toISOString(),
      path: ctx.getRequest().url,
    });
  }
}