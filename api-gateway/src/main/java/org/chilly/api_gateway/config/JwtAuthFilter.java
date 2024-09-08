package org.chilly.api_gateway.config;

import lombok.RequiredArgsConstructor;
import org.chilly.api_gateway.service.JwtService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RefreshScope
@Component
@RequiredArgsConstructor
public class JwtAuthFilter implements GatewayFilter {

    @Value("${http.headers.user-id}")
    private String userIdHeader;

    private final RouteValidator validator;
    private final JwtService jwtService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        if (validator.isOpen(request)) {
            return chain.filter(exchange);
        }
        if (isMissingAuth(request)) {
            return onError(exchange, HttpStatus.UNAUTHORIZED);
        }

        final String token = getAuthToken(request);
        if (token == null) {
            return onError(exchange, HttpStatus.BAD_REQUEST);
        }

        if (jwtService.isNotValid(token)) {
            return onError(exchange, HttpStatus.FORBIDDEN);
        }

        updateRequest(exchange, token);
        return chain.filter(exchange);
    }

    private boolean isMissingAuth(ServerHttpRequest request) {
        return !request.getHeaders().containsKey("Authorization");
    }

    private Mono<Void> onError(ServerWebExchange exchange, HttpStatus status) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(status);
        return response.setComplete();
    }

    private String getAuthToken(ServerHttpRequest request) {
        String value;
        try {
            value = request.getHeaders().getOrEmpty("Authorization").get(0);
        } catch (Exception e) {
            return null;
        }
        if (!value.startsWith("Bearer ")) {
            return null;
        }
        return value.substring(7);
    }

    private void updateRequest(ServerWebExchange exchange, String token) {
        Long userId = jwtService.extractClaim(token, claims -> claims.get(userIdHeader, Long.class));
        exchange.getRequest().mutate()
                .header(userIdHeader, userId.toString())
                .build();
    }
}
