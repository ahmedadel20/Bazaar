package org.bazaar.gateway.config;

import org.bazaar.gateway.service.JwtUtil;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthenticationFilter extends AbstractGatewayFilterFactory<JwtAuthenticationFilter.Config> {
    private final JwtUtil jwtUtil;
    private final RouterValidator validator;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, RouterValidator validator) {
        super(Config.class); // Config class if we need custom configurations later.
        this.jwtUtil = jwtUtil;
        this.validator = validator;
    }


    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            if(!validator.isSecured.test(exchange.getRequest())) {
                return chain.filter(exchange);  // Return statement was missing
            }

            String token = extractToken(exchange);
            if (token == null || !jwtUtil.isTokenValid(token)) {
                return onError(exchange, "Unauthorized Access", HttpStatus.UNAUTHORIZED);
            }

            // Extract claims and add headers
            String bazaarUserId = jwtUtil.extractClaim(token, claims -> claims.get("bazaarUserId", String.class));
            String username = jwtUtil.extractUsername(token);

            exchange = exchange.mutate()
                    .request(r -> r.header("Bazaar-User-Id", bazaarUserId)
                            .header("User-Email", username))
                    .build();

            return chain.filter(exchange);
        };
    }
//    @Override
//    public GatewayFilter apply(Config config) {
//        return (exchange, chain) -> {
//            String token = extractToken(exchange);
//            if(!validator.isSecured.test(exchange.getRequest())) {
//                chain.filter(exchange);
//            }
//            if (token == null || !jwtUtil.isTokenValid(token)) {
//                return onError(exchange, "Unauthorized Access", HttpStatus.UNAUTHORIZED);
//            }
//
//            // Extract claim then put in headers
//            String bazaarUserId = jwtUtil.extractClaim(token, claims -> claims.get("bazaarUserId", String.class));
//            String username = jwtUtil.extractUsername(token);
//            //TODO Extract Role as well
//            //String role = jwtUtil.extractRole(token);
//
////            exchange = exchange.mutate()
////                    .request(r -> r.header("Bazaar-User-Id", bazaarUserId)
////                            .header("User-Email", username))
////                    .build();
////
//            exchange.getRequest().mutate()
//                    .header("Bazaar-User-Id", bazaarUserId)
//                    .header("User-Email", username)
//                    .build();
//
////            exchange = exchange.mutate()
////                    .request(r -> r.header("Bazaar-User-Id", bazaarUserId)
////                            .header("User-Email", username))
////                    .build();
//
//            return chain.filter(exchange);
//        };
//    }

    private String extractToken(ServerWebExchange exchange) {
        String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }

    // Handle unauthorized requests
    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        exchange.getResponse().setStatusCode(httpStatus);
        return exchange.getResponse().setComplete();
    }

    public static class Config {
        // Custom configuration if needed later
    }
}
