package org.chilly.api_gateway.config;

import lombok.RequiredArgsConstructor;
import org.chilly.api_gateway.filters.ApiKeyFilter;
import org.chilly.api_gateway.filters.JwtAuthFilter;
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
                                .path("/api/recs").or()
                                .path("/api/quiz", "/api/quiz/**")
                                .filters(applyFilter(authFilter))
                                .uri("lb://main-svc")
                )
                .route(
                        "security-svc",
                        r -> r.path("/api/auth/**").or()
                                .path("/security-svc/v3/api-docs").or()
                                .path("/api/password", "/api/password/recovery").or()
                                .path("/api/email_code", "/api/email_code/verification").or()
                                .path("/api/auth/roles")
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
                        "places-svc-api-key",
                        r -> r.path("/api/places/ids")
                                .filters(applyFilter(apiKeyFilter))
                                .uri("lb://places-svc")
                )
                .route(
                        "places-svc",
                        r -> r.path( "/api/places", "/api/places/**").or()
                                .path("/api/visits", "/api/visits/**").or()
                                .path("/places-svc/v3/api-docs")
                                .filters(applyFilter(authFilter))
                                .uri("lb://places-svc")
                )
                .route(
                        "feedback-svc",
                        r -> r.path("/api/reviews", "/api/reviews/**").or()
                                .path("/feedback-svc/v3/api-docs")
                                .filters(applyFilter(authFilter))
                                .uri("lb://feedback-svc")
                )
                .route(
                        "business-users-svc",
                        r -> r.path("/api/business_users", "/api/business_users/**").or()
                                .path("/api/business/place_requests", "/api/business/place_requests/**").or()
                                .path("/business-users-svc/v3/api-docs").or()
                                .path("/api/business/places", "/api/business/places/**")
                                .filters(applyFilter(authFilter))
                                .uri("lb://business-users-svc")
                )
                .route(
                        "files-svc",
                        r -> r.path("/api/files/**").or()
                                .path("/files-svc/v3/api-docs")
                                .filters(applyFilter(authFilter))
                                .uri("lb://files-svc")
                )
                .build();
    }

    private Function<GatewayFilterSpec, UriSpec> applyFilter(GatewayFilter filter) {
        return filterSpec -> filterSpec.filter(filter);
    }
}
