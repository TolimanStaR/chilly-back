package org.chilly.api_gateway.authority;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chilly.api_gateway.filters.Role;
import org.chilly.api_gateway.service.JwtService;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpMethod.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class RouteAuthorityChecker {

    private final JwtService jwtService;

    private final Map<Role, List<AllowedEndpoint>> allowedByRole = Map.ofEntries(
            Map.entry(Role.USER, List.of(
                    new AllowedEndpoint("/user/me", GET, PUT),
                    new AllowedEndpoint("api/quiz/question", PUT),
                    new AllowedEndpoint("api/quiz", POST),
                    new AllowedEndpoint("api/questions", GET),
                    new AllowedEndpoint("api/recs", GET),
                    new AllowedEndpoint("api/visits", GET, POST, DELETE),
                    new AllowedEndpoint("api/password", PUT),
                    new AllowedEndpoint("api/places/nearby", GET)
            )),
            Map.entry(Role.ADMIN, List.of(
                    new AllowedEndpoint("api/questions", POST, PUT, GET),
                    new AllowedEndpoint("api/places", POST, PUT, GET),
                    new AllowedEndpoint("api/visits", GET),
                    new AllowedEndpoint("api/places/nearby", GET)
            ))
    );

    /**
     * Verifies that given request has correct authorities
     * @param token retrieved access jwt token
     * @param request incoming request
     * @return true if authorities are verified and correct, false if unable to retrieve authorities, or it is not enough
     */
    public boolean authoritiesMatchRequirements(String token, ServerHttpRequest request) {
        final String requestPath = request.getURI().getPath();
        final HttpMethod requestMethod = request.getMethod();

        List<Role> actualAuthorities = parseJwtAuthorities(token);
        boolean result = actualAuthorities.stream()
                .anyMatch(actual -> requestAllowedByAuthority(requestPath, requestMethod, actual));

        log.info("request: PATH={}, METHOD={}, provided_roles={}, check_result={}", requestPath, requestMethod, actualAuthorities, result);
        return result;
    }

    /**
     * Checks whether specified request is permitted by a provided authority
     * @param path pathPiece of the request
     * @param method method of the request
     * @param role provided authority
     * @return true if request is permitted otherwise false
     */
    private boolean requestAllowedByAuthority(String path, HttpMethod method, Role role) {
        return allowedByRole.get(role).stream()
                .anyMatch(endpoint -> endpoint.methods().contains(method) && path.contains(endpoint.pathPiece()));
    }

    private List<Role> parseJwtAuthorities(String token) {
        String jwtAuthorities = jwtService.extractClaim(token, claims -> claims.get(JwtService.ROLES_KEY, String.class));
        return Arrays.stream(jwtAuthorities.split(","))
                .map(Role::valueOf)
                .toList();
    }
}

