package org.chilly.api_gateway.authority;

import lombok.RequiredArgsConstructor;
import org.chilly.api_gateway.filters.Role;
import org.chilly.api_gateway.service.JwtService;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class RouteAuthorityChecker {

    private final JwtService jwtService;

    private final List<String> noRoleRequired = List.of();

    private final Map<Role, List<AllowedEndpoint>> allowedByRole = Map.ofEntries(
            Map.entry(Role.USER, List.of(
                    new AllowedEndpoint("/user/me", HttpMethod.GET),
                    new AllowedEndpoint("/user/me", HttpMethod.PUT),
                    new AllowedEndpoint("api/quiz/question", HttpMethod.PUT),
                    new AllowedEndpoint("api/quiz", HttpMethod.POST),
                    new AllowedEndpoint("api/questions", HttpMethod.GET),
                    new AllowedEndpoint("api/recs", HttpMethod.GET),
                    new AllowedEndpoint("api/visits", HttpMethod.GET),
                    new AllowedEndpoint("api/visits", HttpMethod.POST),
                    new AllowedEndpoint("api/visits", HttpMethod.DELETE)
            )),
            Map.entry(Role.ADMIN, List.of(
                    new AllowedEndpoint("api/questions", HttpMethod.POST),
                    new AllowedEndpoint("api/questions", HttpMethod.PUT),
                    new AllowedEndpoint("api/questions", HttpMethod.GET),
                    new AllowedEndpoint("api/places", HttpMethod.POST),
                    new AllowedEndpoint("api/visits", HttpMethod.GET)
            ))
    );

    /**
     * Verifies that given request has correct authorities
     * @param token retrieved access jwt token
     * @param request incoming request
     * @return true if authorities are verified and correct, false if unable to retrieve authorities, or it is not enough
     */
    public boolean checkAuthorities(String token, ServerHttpRequest request) {
        final String path = request.getURI().getPath();
        final HttpMethod method = request.getMethod();
        if (noRoleRequired.stream().anyMatch(path::contains)) {
            return true;
        }
        List<Role> actualAuthorities = parseJwtAuthorities(token);
        return actualAuthorities.stream().anyMatch(actual -> requestAllowedByAuthority(path, method, actual));
    }

    private boolean requestAllowedByAuthority(String path, HttpMethod method, Role role) {
        return allowedByRole.get(role).stream()
                .anyMatch(endpoint -> method == endpoint.method() && path.contains(endpoint.path()));
    }

    private List<Role> parseJwtAuthorities(String token) {
        String jwtAuthorities = jwtService.extractClaim(token, claims -> claims.get(JwtService.ROLES_KEY, String.class));
        return Arrays.stream(jwtAuthorities.split(","))
                .map(Role::valueOf)
                .toList();
    }
}

