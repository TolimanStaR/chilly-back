package org.chilly.api_gateway.config;

import lombok.RequiredArgsConstructor;
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
public class ApiKeyFilter implements GatewayFilter {

    @Value("${security.api-key}")
    private String apiKey;

    private static final String API_KEY_HEADER = "x-api-key";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        String token = getAuthKey(request);
        if (token == null) {
            return onError(exchange, HttpStatus.UNAUTHORIZED);
        }

        if (!token.equals(apiKey)) {
            return onError(exchange, HttpStatus.FORBIDDEN);
        }

        return chain.filter(exchange);
    }

    private String getAuthKey(ServerHttpRequest request) {
        try {
            return request.getHeaders().getOrEmpty(API_KEY_HEADER).get(0);
        } catch (Exception e) {
            return null;
        }
    }

    private Mono<Void> onError(ServerWebExchange exchange, HttpStatus status) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(status);
        return response.setComplete();
    }
}
