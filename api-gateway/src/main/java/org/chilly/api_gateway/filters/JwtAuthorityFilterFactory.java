package org.chilly.api_gateway.filters;

import lombok.RequiredArgsConstructor;
import org.chilly.api_gateway.service.JwtService;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.chilly.api_gateway.filters.FilterUtils.getAuthToken;
import static org.chilly.api_gateway.filters.FilterUtils.onError;


@Component
@RequiredArgsConstructor
public class JwtAuthorityFilterFactory {

    private final JwtService jwtService;

    private final Map<String, GatewayFilter> cachedFilters = new HashMap<>();

    /**
     * Creates new filter to check whether request with jwt token has required authority
     * Caches instance and returns cached if already was created
     * @param authority role to check request for
     * @return cached or newly created filter
     */
    public GatewayFilter create(String... authority) {
        String key = keyFor(authority);

        if (!cachedFilters.containsKey(key)) {
            GatewayFilter filter = (exchange, chain) ->
                    filterWithAuthority(exchange, chain, authority);
            cachedFilters.put(key, filter);
        }
        return cachedFilters.get(key);
    }

    private Mono<Void> filterWithAuthority(ServerWebExchange exchange, GatewayFilterChain chain, String... authority) {
        ServerHttpRequest request = exchange.getRequest();
        final String token = getAuthToken(request);
        if (token == null) {
            return onError(exchange, HttpStatus.BAD_REQUEST);
        }
        List<String> jwtAuthorities = parseJwtAuthorities(token);
        if (Arrays.stream(authority).noneMatch(jwtAuthorities::contains)) {
            return onError(exchange, HttpStatus.UNAUTHORIZED);
        }
        return chain.filter(exchange);
    }

    private List<String> parseJwtAuthorities(String token) {
        String jwtAuthorities = jwtService.extractClaim(token, claims -> claims.get(JwtService.ROLES_KEY, String.class));
        return Arrays.asList(jwtAuthorities.split(","));
    }

    private String keyFor(String... authorities) {
        return String.join("", Arrays.stream(authorities).sorted().toList());
    }
}
