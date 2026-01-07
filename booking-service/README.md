# BookingService

BookingService es un microservicio de gestión de reservas de hoteles desarrollado con **Spring Boot**, que forma parte del proyecto de **Reserva-Hoteles-Microservices**

## Características

- CRUD completo de reservas
- Integración con Consul para descubrimiento de servicios
- Manejo de excepciones personalizadas
- Validación de DTOs
- Conexión a PostgreSQL
- Configuración de Kafka para eventos
- Health check integrado

## Tecnologías

- **Spring Boot** (Java 17)
- **Spring Data JPA** para persistencia
- **PostgreSQL** como base de datos
- **Spring Cloud Consul** para descubrimiento de servicios
- **Spring Kafka** para mensajería
- **Validation** para validación de DTOs
- **Actuator** para health check

## Instalación

1. Clona el repositorio
2. Navega al directorio del servicio:
   ```bash
   cd services/booking-service