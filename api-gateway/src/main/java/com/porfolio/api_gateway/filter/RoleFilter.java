package com.porfolio.api_gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;

import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class RoleFilter extends AbstractGatewayFilterFactory<RoleFilter.Config> {

    public RoleFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {

        return (exchange, chain) -> {

            ServerHttpRequest request = exchange.getRequest();

            String userRole = request.getHeaders().getFirst("X-User-Role");
            String userId = request.getHeaders().getFirst("X-User-Id");
            String path = request.getURI().getPath();

            // No role → UNAUTHORIZED
            if (userRole == null) {
                return unauthorized(exchange.getResponse());
            }

            // 1. Check if role is allowed in this route
            if (!config.allowedRoles.contains(userRole)) {
                return forbidden(exchange.getResponse());
            }

            // 2. USER can only modify their own profile
            if (userRole.equals("USER") && path.matches("^/api/users/[^/]+$")) {

                String pathUserId = path.substring(path.lastIndexOf('/') + 1);

                if (!pathUserId.equals(userId)) {
                    return forbidden(exchange.getResponse());
                }
            }

            return chain.filter(exchange);
        };
    }

    private Mono<Void> unauthorized(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return response.setComplete();
    }

    private Mono<Void> forbidden(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.FORBIDDEN);
        return response.setComplete();
    }

    // ----- CONFIG -----
    public static class Config {
        private List<String> allowedRoles;

        public List<String> getAllowedRoles() {
            return allowedRoles;
        }

        public void setAllowedRoles(List<String> allowedRoles) {
            this.allowedRoles = allowedRoles;
        }
    }
}
