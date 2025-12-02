import { Injectable, OnModuleInit, OnModuleDestroy } from '@nestjs/common';
import { ConfigService } from '@nestjs/config';
import Consul from 'consul';

@Injectable()
export class ConsulService implements OnModuleInit, OnModuleDestroy {
  private consul: Consul;
  private serviceId: string;

  constructor(private configService: ConfigService) {
    this.consul = new Consul({
      host: this.configService.get('CONSUL_HOST', 'localhost'),
      port: this.configService.get('CONSUL_PORT', 8500),
    });

    this.serviceId = `auth-service-${Date.now()}`;
  }

  async onModuleInit() {
    const service = {
      id: this.serviceId,
      name: 'auth-service',
      address: 'host.docker.internal',
      port: parseInt(this.configService.get('PORT', '3000'), 10),
      tags: ['auth', 'nestjs', 'jwt'],
      check: {
        name: 'auth-service-health-check',
        http: `http://host.docker.internal:${this.configService.get('PORT', '3000')}/api/health`,
        interval: '10s',
        timeout: '5s',
      },
    };

    try {
      await this.consul.agent.service.register(service);
      console.log(`AuthService registered in Consul with ID: ${this.serviceId}`);
    } catch (error) {
      console.error('Error registering AuthService in Consul:', error);
    }
  }

  async onModuleDestroy() {
    try {
      await this.consul.agent.service.deregister(this.serviceId);
      console.log(`AuthService deregistered from Consul with ID: ${this.serviceId}`);
    } catch (error) {
      console.error('Error deregistering AuthService from Consul:', error);
    }
  }

  async getServices() {
    try {
      const services = await this.consul.agent.services();
      return services;
    } catch (error) {
      console.error('Error getting services from Consul:', error);
      return {};
    }
  }

  async getService(serviceName: string) {
    try {
      const services = await this.consul.health.service({
        service: serviceName,
        passing: true,
      });
      return services;
    } catch (error) {
      console.error(`Error getting service ${serviceName} from Consul:`, error);
      return [];
    }
  }
}