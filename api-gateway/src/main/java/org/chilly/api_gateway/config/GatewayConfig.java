package org.chilly.api_gateway.config;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.GatewayFilterSpec;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.gateway.route.builder.UriSpec;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;

@Configuration
@RequiredArgsConstructor
public class GatewayConfig {

    private final JwtAuthFilter authFilter;
    private final ApiKeyFilter apiKeyFilter;

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder routeLocatorBuilder) {
        return routeLocatorBuilder.routes()
                .route(
                        "main-svc",
                        r -> r.path("/api/user", "/api/user/me").or()
                                .path("/main-svc/v3/api-docs").or()
                                .path("/api/questions").or()
                                .path("/api/visits", "/api/visits/**").or()
                                .path("/api/recs").or()
                                .path("/api/places").or()
                                .path("/api/quiz", "/api/quiz/**")
                                .filters(applyFilter(authFilter))
                                .uri("lb://main-svc")
                )
                .route(
                        "security-svc",
                        r -> r.path("/api/auth/**", "/security-svc/v3/api-docs").or()
                                .path("/api/password").or()
                                .path("/api/email_code").or()
                                .path("/api/password/recovery").or()
                                .path("/api/email_code/verification")
                                .filters(applyFilter(authFilter))
                                .uri("lb://security-svc")
                )
                .route(
                        "rec-svc",
                        r -> r.path("/api/prediction")
                                .filters(applyFilter(apiKeyFilter))
                                .uri("lb://rec-svc")
                )
                .route(
                        "places-ids",
                        r -> r.path("/api/places/ids")
                                .filters(applyFilter(apiKeyFilter))
                                .uri("lb://main-svc")
                )
                .build();
    }

    private Function<GatewayFilterSpec, UriSpec> applyFilter(GatewayFilter filter) {
        return filterSpec -> filterSpec.filter(filter);
    }
}
