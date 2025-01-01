package org.bazaar.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public GatewayConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("auth-service",r -> r.path("/api/v1/auth/**")
                        .filters(f -> f.filter(jwtAuthenticationFilter.apply(new JwtAuthenticationFilter.Config())))
                        .uri("http://localhost:8081"))


                .route("customer-service", r -> r.path("/api/v1/customer/**")
                        .filters(f -> f.filter(jwtAuthenticationFilter.apply(new JwtAuthenticationFilter.Config())))
                        .uri("http://localhost:8081")).build();
    }
}
