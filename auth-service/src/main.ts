import { NestFactory } from '@nestjs/core';
import { AppModule } from './app.module';
import { ValidationPipe } from '@nestjs/common';
import { BusinessExceptionFilter } from './common/filters/business-exception.filter';
import { DatabaseExceptionFilter } from './common/filters/database-exception.filter';
import { NotFoundExceptionFilter } from './common/filters/not-found-exception.filter';
import cookieParser from 'cookie-parser';

async function bootstrap() {
  const app = await NestFactory.create(AppModule);
  app.useGlobalPipes(new ValidationPipe({ whitelist: true, forbidNonWhitelisted: true }));
  app.useGlobalFilters(
    new BusinessExceptionFilter(),
    new DatabaseExceptionFilter(),
    new NotFoundExceptionFilter(),
  );
  app.use(cookieParser());
  app.setGlobalPrefix('api');
  await app.listen(process.env.PORT ?? 3000);
}
bootstrap();
