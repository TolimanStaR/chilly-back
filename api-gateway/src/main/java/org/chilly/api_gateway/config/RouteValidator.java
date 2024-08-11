package org.chilly.api_gateway.config;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class RouteValidator {

    private final List<String> openEndPoints = List.of(
            "/api/auth/register",
            "/api/auth/login",
            "/api/auth/refresh"
    );

    private final Predicate<ServerHttpRequest> securedPredicate = (request) -> {
        final String path = request.getURI().getPath();
        return openEndPoints.stream().noneMatch(path::contains);
    };

    public boolean isOpen(ServerHttpRequest request) {
        return !securedPredicate.test(request);
    }

}
