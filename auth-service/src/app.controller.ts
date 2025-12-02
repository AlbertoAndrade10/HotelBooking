import { Controller, Get } from '@nestjs/common';
import { AppService } from './app.service';
import { ConsulService } from './consul/consul.service';

@Controller()
export class AppController {
    constructor(
        private readonly appService: AppService,
        private readonly consulService: ConsulService,
    ) { }

    @Get('health')
    getHealth(): string {
        return this.appService.getHealth();
    }

    @Get('consul/services')
    async getConsulServices() {
        return await this.consulService.getServices();
    }

    @Get('consul/service/:name')
    async getConsulService(name: string) {
        return await this.consulService.getService(name);
    }
}