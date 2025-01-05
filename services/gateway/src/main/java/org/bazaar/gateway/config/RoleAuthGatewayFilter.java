package org.bazaar.gateway.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.bazaar.gateway.service.JwtUtil;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Component
public class RoleAuthGatewayFilter extends AbstractGatewayFilterFactory<RoleAuthGatewayFilter.Config> {

    private final JwtUtil jwtUtil;

    private final RouterValidator routerValidator;

    public RoleAuthGatewayFilter(JwtUtil jwtUtil, RouterValidator routerValidator) {
        super(Config.class);
        this.jwtUtil = jwtUtil;
        this.routerValidator = routerValidator;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {

            // Extract the token
            String token = extractToken(exchange);
            if(!routerValidator.isSecured.test(exchange.getRequest())) {
                return chain.filter(exchange);
            }

            if (token == null || !jwtUtil.isTokenValid(token)) {
                // If Token is invalid
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            // Extract roles from the token
            @SuppressWarnings("unchecked")
            List<String> roles = jwtUtil.extractClaim(token, claims -> claims.get("roles", List.class));

            // Check if any of the roles match the required roles
            boolean hasRequiredRole = roles.stream().anyMatch(config.getRoles()::contains);

            if (!hasRequiredRole) {
                return onError(exchange, "Forbidden - Insufficient Permissions", HttpStatus.FORBIDDEN);
            }

            // Add headers for downstream services
            Long bazaarUserId = jwtUtil.extractClaim(token, claims -> claims.get("bazaarUserId", Long.class));
            String username = jwtUtil.extractUsername(token);
            String rolesHeader = String.join(",", roles);

            //FIXME Update and pass any admins through
            if (roles.contains("CUSTOMER") && !roles.contains("ADMIN")) {

                //Extract the path variables from the incoming request
                Map<String, String> pathParams = exchange.getAttribute(ServerWebExchangeUtils.URI_TEMPLATE_VARIABLES_ATTRIBUTE);

                //Extract the path variable {customerId} from the incoming request
                Long customerId = Long.valueOf(pathParams.get("customerId"));

                if (!bazaarUserId.equals(customerId)) { // Ownership validation
                    return onError(exchange, "Forbidden - Access Denied", HttpStatus.FORBIDDEN);
                }
            }

            exchange = exchange.mutate()
                    .request(r -> r.header("Bazaar-User-Id", bazaarUserId.toString())
                            .header("User-Email", username)
                            .header("Roles", rolesHeader))
                    .build();

            return chain.filter(exchange);
        };
    }

    private String extractToken(ServerWebExchange exchange) {
        String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        exchange.getResponse().setStatusCode(httpStatus);
        return exchange.getResponse().setComplete();
    }

    @Data
    @NoArgsConstructor
    public static class Config {
        private List<String> roles;

        public Config(List<String> roles){
            this.roles = roles;
        }
    }
}
