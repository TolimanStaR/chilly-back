package org.chilly.api_gateway.filters;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chilly.api_gateway.authority.RouteAuthorityChecker;
import org.chilly.api_gateway.config.RouteValidator;
import org.chilly.api_gateway.service.JwtService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static org.chilly.api_gateway.filters.FilterUtils.*;

@RefreshScope
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthFilter implements GatewayFilter {

    @Value("${http.headers.user-id}")
    private String userIdHeader;

    private final RouteValidator validator;
    private final RouteAuthorityChecker authorityChecker;
    private final JwtService jwtService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        log.info("got request to filter: path={}", request.getURI().getPath());

        if (validator.isOpen(request)) {
            log.info("request is OPEN");
            return chain.filter(exchange);
        }
        if (isMissingAuth(request)) {
            log.error("NO AUTH HEADER PROVIDED");
            return onError(exchange, HttpStatus.UNAUTHORIZED);
        }

        final String token = getAuthToken(request);
        if (token == null) {
            return onError(exchange, HttpStatus.BAD_REQUEST);
        }

        if (jwtService.isNotValid(token)) {
            log.error("TOKEN IS INVALID");
            return onError(exchange, HttpStatus.FORBIDDEN);
        }

        if (!authorityChecker.authoritiesMatchRequirements(token, request)) {
            log.error("REQUEST DOES NOT SATISFY AUTHORITY REQUIREMENTS");
            return onError(exchange, HttpStatus.UNAUTHORIZED);
        }

        updateRequest(exchange, token);
        return chain.filter(exchange);
    }

    private void updateRequest(ServerWebExchange exchange, String token) {
        Long userId = jwtService.extractClaim(token, claims -> claims.get(userIdHeader, Long.class));
        exchange.getRequest().mutate()
                .header(userIdHeader, userId.toString())
                .build();
    }
}
