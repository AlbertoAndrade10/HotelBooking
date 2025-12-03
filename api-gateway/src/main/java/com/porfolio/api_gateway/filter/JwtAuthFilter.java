package com.porfolio.api_gateway.filter;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class JwtAuthFilter extends AbstractGatewayFilterFactory<JwtAuthFilter.Config> {

    @Value("${jwt.secret}")
    private String jwtSecret;

    private final ReactiveRedisTemplate<String, String> redis;

    public JwtAuthFilter(ReactiveRedisTemplate<String, String> redis) {
        super(Config.class);
        this.redis = redis;
    }

    @Override
    public GatewayFilter apply(Config config) {

        return (exchange, chain) -> {

            ServerHttpRequest request = exchange.getRequest();
            String path = request.getURI().getPath();

            // 1 → public routes
            if (isPublicRoute(path)) {
                return chain.filter(exchange);
            }

            // 2 → verify Authorization header
            if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                return unauthorized(exchange.getResponse());
            }

            String token = extractToken(request);

            if (token == null || !isTokenValid(token)) {
                return unauthorized(exchange.getResponse());
            }

            // 3 → extract claims
            Claims claims = extractClaims(token);
            String userId = claims.get("sub", String.class);
            Integer tokenVersionInJwt = claims.get("ver", Integer.class);

            if (userId == null || tokenVersionInJwt == null) {
                return unauthorized(exchange.getResponse());
            }

            String redisKey = "token_version:" + userId;

            // 4 → validate token version from Redis
            return redis.opsForValue().get(redisKey)
                    .defaultIfEmpty("0")
                    .flatMap(versionStr -> {

                        int tokenVersionInRedis;

                        try {
                            tokenVersionInRedis = Integer.parseInt(versionStr);
                        } catch (NumberFormatException e) {
                            return unauthorized(exchange.getResponse());
                        }

                        if (tokenVersionInRedis != tokenVersionInJwt) {
                            return unauthorized(exchange.getResponse());
                        }

                        // 5 → forward claims to microservices
                        ServerHttpRequest modifiedRequest = exchange.getRequest().mutate()
                                .header("X-User-Id", userId)
                                .header("X-User-Email", claims.get("email", String.class))
                                .header("X-User-Role", claims.get("role", String.class))
                                .build();

                        return chain.filter(exchange.mutate().request(modifiedRequest).build());
                    });
        };
    }

    // -------------------------
    // VALIDATIONS AND HELPERS
    // -------------------------

    private boolean isPublicRoute(String path) {

        List<String> publicRoutes = List.of(
                "/api/auth/login",
                "/api/auth/register",
                "/api/auth/refresh",
                "/actuator/health");

        return publicRoutes.stream().anyMatch(path::startsWith);
    }

    private String extractToken(ServerHttpRequest request) {
        String bearer = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (bearer != null && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }

    private boolean isTokenValid(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(jwtSecret.getBytes(StandardCharsets.UTF_8))
                    .build()
                    .parseClaimsJws(token);

            return true;

        } catch (JwtException e) {
            return false;
        }
    }

    private Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(jwtSecret.getBytes(StandardCharsets.UTF_8))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Mono<Void> unauthorized(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return response.setComplete();
    }

    public static class Config {
    }
}
