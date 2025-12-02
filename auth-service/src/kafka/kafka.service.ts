import { Injectable, OnModuleInit } from "@nestjs/common";
import { ConfigService } from "@nestjs/config";
import { Kafka, Producer } from "kafkajs";

@Injectable()
export class KafkaService implements OnModuleInit {
    private producer: Producer;

    constructor(private config: ConfigService) {
        const kafka = new Kafka({
            clientId: 'auth-service',
            brokers: [config.get('KAFKA_BROKERS', 'localhost:9093')],
        });

        this.producer = kafka.producer();
    }

    async onModuleInit() {
        await this.producer.connect();
    }

    // register event
    async sendUserRegisteredEvent(userData: {
        id: string;
        email: string;
        role: string;
    }) {
        await this.producer.send({
            topic: 'user-registered',
            messages: [
                {
                    value: JSON.stringify({
                        ...userData,
                        timestamp: new Date().toISOString(),
                    }),
                },
            ],
        });
    }

    // event when user updated email or role
    async sendUserUpdatedEvent(userData: {
        id: string;
        email?: string;
        role?: string;
    }) {
        await this.producer.send({
            topic: 'user-updated',
            messages: [
                {
                    value: JSON.stringify({
                        ...userData,
                        timestamp: new Date().toISOString(),
                    }),
                },
            ],
        });
    }

}
