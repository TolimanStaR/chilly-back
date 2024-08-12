package org.chilly.api_gateway.config;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.GatewayFilterSpec;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.gateway.route.builder.UriSpec;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

@Configuration
@RequiredArgsConstructor
public class GatewayConfig {

    private final JwtAuthFilter authFilter;

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder routeLocatorBuilder) {
        return routeLocatorBuilder.routes()
                .route(
                        "main-svc",
                        r -> r.path("/main-svc/v3/api-docs").or()
                                .path("/api/user").and().method(HttpMethod.GET)
                                .filters(this::applyAuthFilter)
                                .uri("lb://main-svc")
                )
                .route(
                        "security-svc",
                        r -> r.path("/api/auth/**", "/security-svc/v3/api-docs")
                                .filters(this::applyAuthFilter)
                                .uri("lb://security-svc")
                )
                .build();
    }

    private UriSpec applyAuthFilter(GatewayFilterSpec filterSpec) {
        return filterSpec.filter(authFilter);
    }
}
