# AuthService

AuthService es un microservicio de autenticación y autorización desarrollado con **NestJS**, que forma parte del proyecto **Reserva-Hoteles-Microservices**

## Características

- Registro e inicio de sesión de usuarios
- Manejo de tokens JWT (access y refresh)
- Almacenamiento de refresh tokens en Redis
- Integración con PostgreSQL para persistencia de usuarios
- Manejo de excepciones personalizadas
- Validación de DTOs
- Health check

## Flujo del servicio

- **1 Registro de Usuario** 
    **POST** /auth/register
    1. Verifica si el email existe
    2. Hashea la contraseña (usando bcrypt)
    3. Guarda al usuario
    4. Emite un evento Kafka (user_registered)
    5. Devuelve el id del usuario

- **2 Login de Usuario** 
    **POST** /auth/login
    1. El usuario envía email y password
    2. Se validan email y contraseña
    3. Se general accessToken (15min) y refreshToken (7days)
    4. El refreshToken se guarda en Redis (refresh_token:<userId>)
    5. El refreshToken se envía al cliente vía cookie HTTP-only
    
    - AccessToken
        - Usados para acceder a recursos protegidos
        - Validación mediante JwtStrategy
        - Incluyen: sub, email, role, type=access
        - Enbebidos en header authorization: Bearer<token>
    
    - RefreshToken y rotación
        - Los refreshToken:
            - Se guardan en Redis
            - Se renuevan en cada refresh
            - Se validan contra lo que hay en Redis, Si no coincide -> el token es invalido
            - Previene de ataques replay y robo de tokens

- **3 Refresh Tokens**
   **POST** /auth/refresh
    - Protegidos por Guards (@UseGuards(RefreshTokenGuard))
    - Flujo:
        - RefreshTokenGuard extrae el token de la cookie
        - Valida la firma (JWT_REFRESH_TOKEN)
        - Se verifica que su valor coincida con Redis
        - Se generan nuevos:
            - accessToken
            - refreshToken
        - Se actualiza Redis
        - Se envía el refreshToken actualizado en la cookie

- **4 Logout**
   **POST** /auth/logout
    - Flujo:
        - Se lee el refreshToken de la cookie
        - Se elimina de Redis
        - Se limpia el cookie del cliente 

- **5 Change Password**
   **Patch** /auth/refresh
    1. Protegido por JwtAuthGuard
    2. Flujo:
        - Se valida currentPassword
        - Se actualiza la contraseña
        - Se invalida refreshToken en Redis
        - El cliente debe hacer login de nuevo

## Tecnologías

- **NestJS** (TypeScript)
- **TypeORM** para persistencia
- **PostgreSQL** como base de datos
- **Redis** para almacenamiento de tokens
- **JWT** para autenticación
- **Passport** para estrategias de autenticación
- **class-validator** para validación de DTOs

## Instalación

1. Clona el repositorio
2. Navega al directorio del servicio:
   ```bash
   cd services/auth-service
3. Instala las dependencias
    ```bash
    npm install
4. Lanza el microservicio (Modo desarrollo)
    ```bash
    npm run start:dev