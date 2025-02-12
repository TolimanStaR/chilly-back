package org.chilly.api_gateway.filters;

import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public class FilterUtils {
    public static boolean isMissingAuth(ServerHttpRequest request) {
        return !request.getHeaders().containsKey("Authorization");
    }

    public static Mono<Void> onError(ServerWebExchange exchange, HttpStatus status) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(status);
        return response.setComplete();
    }

    public static String getAuthToken(ServerHttpRequest request) {
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
}
