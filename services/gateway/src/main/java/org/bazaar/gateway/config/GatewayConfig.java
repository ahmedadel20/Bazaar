package org.bazaar.gateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

import java.util.List;

@Configuration
public class GatewayConfig {

    @Autowired
    private final RoleAuthGatewayFilter roleAuthGatewayFilter;

    public GatewayConfig(RoleAuthGatewayFilter roleAuthGatewayFilter) {
        this.roleAuthGatewayFilter = roleAuthGatewayFilter;
    }

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()

                // Public routes (No authentication required)
                .route("auth-service", r -> r.path("/api/v1/auth/**")
                        .uri("http://localhost:8081"))

                // User Management Service
                // Customer Routes

                // Get all customers
                .route("get-all-customers", r -> r.path("/api/v1/customer")
                        .and().method(HttpMethod.GET)
                        .uri("http://localhost:8081"))

                // Get a single customer
                .route("get-customer", r -> r.path("/api/v1/customer/{customerId}")
                        .and().method(HttpMethod.GET)
                        .uri("http://localhost:8081"))

                // Update a customer
                .route("update-customer", r -> r.path("/api/v1/customer/{customerId}")
                        .and().method(HttpMethod.PUT)
                        .uri("http://localhost:8081"))

                // Delete a customer
                .route("delete-customer", r -> r.path("/api/v1/customer/{customerId}")
                        .and().method(HttpMethod.DELETE)
                        .uri("http://localhost:8081"))
                // Add address to a customer
                .route("add-address", r -> r.path("/api/v1/customer/{customerId}/addAddress")
                        .and().method(HttpMethod.POST)
                        .uri("http://localhost:8081"))

                // Remove address from a customer
                .route("remove-address", r -> r.path("/api/v1/customer/{customerId}/removeAddress")
                        .and().method(HttpMethod.DELETE)
                        .uri("http://localhost:8081"))

                // Order Management Service

                // Order Routes
                // Customer route for creating an order
                .route("create-order", r -> r.path("/api/v1/{customerId}/order")
                        .and().method(HttpMethod.POST)
                        .filters(f -> f.filter(roleAuthGatewayFilter
                                .apply(new RoleAuthGatewayFilter.Config(List.of("ADMIN", "CUSTOMER")))))
                        .uri("http://localhost:8082"))

                // All roles can fetch a specific order
                .route("get-order", r -> r.path("/api/v1/{customerId}/order/{orderId}")
                        .and().method(HttpMethod.GET)
                        .filters(f -> f.filter(roleAuthGatewayFilter
                                .apply(new RoleAuthGatewayFilter.Config(List.of("CUSTOMER", "MANAGER", "ADMIN")))))
                        .uri("http://localhost:8082"))

                // Admin-only route for fetching all orders
                .route("get-all-orders", r -> r.path("/api/v1/{customerId}/order")
                        .and().method(HttpMethod.GET)
                        .filters(f -> f.filter(roleAuthGatewayFilter
                                .apply(new RoleAuthGatewayFilter.Config(List.of("ADMIN", "MANAGER")))))
                        .uri("http://localhost:8082"))

                //Admin and Manager route for deleting an order
                .route("delete-order", r -> r.path("/api/v1/{customerId}/order/{orderId}")
                        .and().method(HttpMethod.DELETE)
                        .filters(f -> f.filter(roleAuthGatewayFilter
                                .apply(new RoleAuthGatewayFilter.Config(List.of("ADMIN", "MANAGER")))))
                        .uri("http://localhost:8082"))

                //------------------------------------------------------------------------------------------------//

                //Transaction Routes
                // Admin-only route for fetching all transactions
                .route("get-all-transactions", r -> r.path("/api/v1/{customerId}/transaction")
                        .and().method(HttpMethod.GET)
                        .filters(f -> f.filter(roleAuthGatewayFilter
                                .apply(new RoleAuthGatewayFilter.Config(List.of("ADMIN")))))
                        .uri("http://localhost:8082"))

                // Customer-only route for creating a transaction
                .route("create-transaction", r -> r.path("/api/v1/{customerId}/transaction")
                        .and().method(HttpMethod.POST)
                        .filters(f -> f.filter(roleAuthGatewayFilter
                                .apply(new RoleAuthGatewayFilter.Config(List.of("ADMIN", "CUSTOMER")))))
                        .uri("http://localhost:8082"))

                // Admin and customer route for fetching a specific transaction
                .route("get-transaction", r -> r.path("/api/v1/{customerId}/transaction/{transactionId}")
                        .and().method(HttpMethod.GET)
                        .filters(f -> f.filter(roleAuthGatewayFilter
                                .apply(new RoleAuthGatewayFilter.Config(List.of("ADMIN")))))
                        .uri("http://localhost:8082"))

                // Admin-only route for updating a transaction
                .route("update-transaction", r -> r.path("/api/v1/{customerId}/transaction/{transactionId}")
                        .and().method(HttpMethod.PUT)
                        .filters(f -> f.filter(roleAuthGatewayFilter
                                .apply(new RoleAuthGatewayFilter.Config(List.of("ADMIN")))))
                        .uri("http://localhost:8082"))

                // Admin-only route for deleting a transaction
                .route("delete-transaction", r -> r.path("/api/v1/{customerId}/transaction/{transactionId}")
                        .and().method(HttpMethod.DELETE)
                        .filters(f -> f.filter(roleAuthGatewayFilter
                                .apply(new RoleAuthGatewayFilter.Config(List.of("ADMIN")))))
                        .uri("http://localhost:8082"))

                //------------------------------------------------------------------------------------------------//

                // Inventory Service

                // Cart Item Routes
                // Adding a cart item
                .route("add-to-cart", r -> r.path("/api/v1/{customerId}/cart-items")
                        .and().method(HttpMethod.POST)
                        .filters(f -> f.filter(roleAuthGatewayFilter
                                .apply(new RoleAuthGatewayFilter.Config(List.of("ADMIN", "CUSTOMER")))))
                        .uri("http://localhost:8083"))

                // Getting all cart items
                .route("get-all-cart-items", r -> r.path("/api/v1/{customerId}/cart-items")
                        .and().method(HttpMethod.GET)
                        .filters(f -> f.filter(roleAuthGatewayFilter
                                .apply(new RoleAuthGatewayFilter.Config(List.of("ADMIN", "CUSTOMER")))))
                        .uri("http://localhost:8083"))

                // Getting a specific cart item
                .route("get-cart-item", r -> r.path("/api/v1/{customerId}/cart-items/{cartItemId}")
                        .and().method(HttpMethod.GET)
                        .filters(f -> f.filter(roleAuthGatewayFilter
                                .apply(new RoleAuthGatewayFilter.Config(List.of("ADMIN", "CUSTOMER")))))
                        .uri("http://localhost:8083"))

                // Updating a cart item
                .route("update-cart-item", r -> r.path("/api/v1/{customerId}/cart-items/{cartItemId}")
                        .and().method(HttpMethod.POST)
                        .filters(f -> f.filter(roleAuthGatewayFilter
                                .apply(new RoleAuthGatewayFilter.Config(List.of("ADMIN", "CUSTOMER")))))
                        .uri("http://localhost:8083"))

                // Updating a cart Item Quantity
                .route("update-cart-item-quantity", r -> r.path("/api/v1/{customerId}/cart-items/{cartItemId}/update-quantity")
                        .and().method(HttpMethod.PUT)
                        .filters(f -> f.filter(roleAuthGatewayFilter
                                .apply(new RoleAuthGatewayFilter.Config(List.of("ADMIN", "CUSTOMER")))))
                        .uri("http://localhost:8083"))

                // Deleting a cart item
                .route("delete-cart-item", r -> r.path("/api/v1/{customerId}/cart-items/{cartItemId}")
                        .and().method(HttpMethod.DELETE)
                        .filters(f -> f.filter(roleAuthGatewayFilter
                                .apply(new RoleAuthGatewayFilter.Config(List.of("ADMIN", "CUSTOMER")))))
                        .uri("http://localhost:8083"))

                // Deleting all cart items
                .route("delete-all-cart-items", r -> r.path("/api/v1/{customerId}/cart-items")
                        .and().method(HttpMethod.DELETE)
                        .filters(f -> f.filter(roleAuthGatewayFilter
                                .apply(new RoleAuthGatewayFilter.Config(List.of("ADMIN", "CUSTOMER")))))
                        .uri("http://localhost:8083"))

                //------------------------------------------------------------------------------------------------//

                // Category Routes
                // Getting all categories
                .route("get-all-categories", r -> r.path("/api/v1/categories")
                        .and().method(HttpMethod.GET)
                        .filters(f -> f.filter(roleAuthGatewayFilter
                                .apply(new RoleAuthGatewayFilter.Config(List.of("ADMIN", "CUSTOMER")))))
                        .uri("http://localhost:8083"))

                // Getting a specific category
                .route("get-category", r -> r.path("/api/v1/categories/{categoryId}")
                        .and().method(HttpMethod.GET)
                        .filters(f -> f.filter(roleAuthGatewayFilter
                                .apply(new RoleAuthGatewayFilter.Config(List.of("ADMIN", "CUSTOMER")))))
                        .uri("http://localhost:8083"))

                // Adding a category
                .route("add-category", r -> r.path("/api/v1/categories")
                        .and().method(HttpMethod.POST)
                        .filters(f -> f.filter(roleAuthGatewayFilter
                                .apply(new RoleAuthGatewayFilter.Config(List.of("ADMIN", "MANAGER")))))
                        .uri("http://localhost:8083"))

                // Updating a category
                .route("update-category", r -> r.path("/api/v1/categories")
                        .and().method(HttpMethod.PUT)
                        .filters(f -> f.filter(roleAuthGatewayFilter
                                .apply(new RoleAuthGatewayFilter.Config(List.of("ADMIN", "MANAGER")))))
                        .uri("http://localhost:8083"))

                // Deleting a category
                .route("delete-category", r -> r.path("/api/v1/categories/{categoryId}")
                        .and().method(HttpMethod.DELETE)
                        .filters(f -> f.filter(roleAuthGatewayFilter
                                .apply(new RoleAuthGatewayFilter.Config(List.of("ADMIN", "MANAGER")))))
                        .uri("http://localhost:8083"))

                //------------------------------------------------------------------------------------------------//

                //Product Routes
                // Getting all products
                .route("get-all-products", r -> r.path("/api/v1/products")
                        .and().method(HttpMethod.GET)
                        .filters(f -> f.filter(roleAuthGatewayFilter
                                .apply(new RoleAuthGatewayFilter.Config(List.of("ADMIN", "CUSTOMER", "MANAGER")))))
                        .uri("http://localhost:8083"))

                // Getting a specific product
                .route("get-product", r -> r.path("/api/v1/products/{productId}")
                        .and().method(HttpMethod.GET)
                        .filters(f -> f.filter(roleAuthGatewayFilter
                                .apply(new RoleAuthGatewayFilter.Config(List.of("ADMIN", "CUSTOMER", "MANAGER")))))
                        .uri("http://localhost:8083"))

                // Adding a product
                .route("add-product", r -> r.path("/api/v1/products")
                        .and().method(HttpMethod.POST)
                        .filters(f -> f.filter(roleAuthGatewayFilter
                                .apply(new RoleAuthGatewayFilter.Config(List.of("ADMIN", "MANAGER")))))
                        .uri("http://localhost:8083"))

                // Updating a product
                .route("update-product", r -> r.path("/api/v1/products")
                        .and().method(HttpMethod.PUT)
                        .filters(f -> f.filter(roleAuthGatewayFilter
                                .apply(new RoleAuthGatewayFilter.Config(List.of("ADMIN", "MANAGER")))))
                        .uri("http://localhost:8083"))

                // Deleting a product
                .route("delete-product", r -> r.path("/api/v1/products/{productId}")
                        .and().method(HttpMethod.DELETE)
                        .filters(f -> f.filter(roleAuthGatewayFilter
                                .apply(new RoleAuthGatewayFilter.Config(List.of("ADMIN", "MANAGER")))))
                        .uri("http://localhost:8083"))

                //Apply discount to products
                .route("apply-discount", r -> r.path("/api/v1/products/updateprices")
                        .and().method(HttpMethod.PUT)
                        .filters(f -> f.filter(roleAuthGatewayFilter
                                .apply(new RoleAuthGatewayFilter.Config(List.of("ADMIN", "MANAGER")))))
                        .uri("http://localhost:8083"))

                // Get Products by Categories
                .route("get-products-by-categories", r -> r.path("/api/v1/products/bycategories")
                        .and().method(HttpMethod.GET)
                        .filters(f -> f.filter(roleAuthGatewayFilter
                                .apply(new RoleAuthGatewayFilter.Config(List.of("ADMIN", "MANAGER", "CUSTOMER")))))
                        .uri("http://localhost:8083"))

                // Get List of Products
                .route("get-list-of-products", r -> r.path("/api/v1/products/listofproducts")
                        .and().method(HttpMethod.GET)
                        .filters(f -> f.filter(roleAuthGatewayFilter
                                .apply(new RoleAuthGatewayFilter.Config(List.of("ADMIN", "MANAGER", "CUSTOMER")))))
                        .uri("http://localhost:8083"))

                //------------------------------------------------------------------------------------------------//

                // Product Catalogue Routes
                // Sale Routes
                // Create a sale
                .route("create-sale", r -> r.path("/api/v1/sale")
                        .and().method(HttpMethod.POST)
                        .filters(f -> f.filter(roleAuthGatewayFilter
                                .apply(new RoleAuthGatewayFilter.Config(List.of("ADMIN", "MANAGER")))))
                        .uri("http://localhost:8084"))

                // Get all sales
                .route("get-all-sales", r -> r.path("/api/v1/sale")
                        .and().method(HttpMethod.GET)
                        .filters(f -> f.filter(roleAuthGatewayFilter
                                .apply(new RoleAuthGatewayFilter.Config(List.of("ADMIN", "MANAGER")))))
                        .uri("http://localhost:8084"))

                // Get a specific sale
                .route("get-sale", r -> r.path("/api/v1/sale/{saleId}")
                        .and().method(HttpMethod.GET)
                        .filters(f -> f.filter(roleAuthGatewayFilter
                                .apply(new RoleAuthGatewayFilter.Config(List.of("ADMIN", "MANAGER")))))
                        .uri("http://localhost:8084"))

                // Delete a sale
                .route("delete-sale", r -> r.path("/api/v1/sale/{saleId}")
                        .and().method(HttpMethod.DELETE)
                        .filters(f -> f.filter(roleAuthGatewayFilter
                                .apply(new RoleAuthGatewayFilter.Config(List.of("ADMIN", "MANAGER")))))
                        .uri("http://localhost:8084"))

                //------------------------------------------------------------------------------------------------//

                .build();
    }
}