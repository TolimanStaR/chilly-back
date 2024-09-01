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
                        r -> r.path("/api/user").and().method(HttpMethod.GET).or()
                                .path("/main-svc/v3/api-docs").or()
                                .path("/api/questions").or()
                                .path("/api/recs").or()
                                .path("/api/quiz", "/api/quiz/**")
                                .filters(this::applyAuthFilter)
                                .uri("lb://main-svc")
                )
                .route(
                        "security-svc",
                        r -> r.path("/api/auth/**", "/security-svc/v3/api-docs").or()
                                .path("/api/password").or()
                                .path("/api/email_code").or()
                                .path("/api/password/recovery").or()
                                .path("/api/email_code/verification")
                                .filters(this::applyAuthFilter)
                                .uri("lb://security-svc")
                )
                .build();
    }

    private UriSpec applyAuthFilter(GatewayFilterSpec filterSpec) {
        return filterSpec.filter(authFilter);
    }
}
