//package com.ritesh.api_gateway.config;
//
//import org.springframework.cloud.gateway.route.RouteLocator;
//import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class GatewayConfig {
//
//    @Bean
//    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
//
//        return builder.routes()
//
//                // Product Service
//                .route("product-service", r -> r
//                        .path("/products/**")
//                        .filters(f -> f.rewritePath(
//                                "/products/(?<segment>.*)",
//                                "/api/v1/products/${segment}"
//                        ))
//                        .uri("http://localhost:8081"))
//
//                // Cart Service
//                .route("cart-service", r -> r
//                        .path("/users/*/cart/**")
//                        .filters(f -> f.rewritePath(
//                                "/users/(?<userId>[^/]+)/cart(?<remaining>/.*)?",
//                                "/api/v1/users/${userId}/cart${remaining}"
//                        ))
//                        .uri("http://localhost:8083"))
//
//                // Order Service
//                .route("order-service", r -> r
//                        .path("/users/*/orders/**")
//                        .filters(f -> f.rewritePath(
//                                "/users/(?<userId>[^/]+)/orders(?<remaining>/.*)?",
//                                "/api/v1/users/${userId}/orders${remaining}"
//                        ))
//                        .uri("http://localhost:8084"))
//
//                // User Service
//                .route("user-service", r -> r
//                        .path("/users", "/users/**")
//                        .filters(f -> f.rewritePath(
//                                "/users/(?<segment>.*)",
//                                "/api/v1/users/${segment}"
//                        ))
//                        .uri("http://localhost:8082"))
//
//                .build();
//    }
//}

package com.ritesh.api_gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {

        return builder.routes()

                // Product Service
                .route("product-service", r -> r
                        .path("/api/v1/products", "/api/v1/products/**")
                        .uri("http://localhost:8081"))

                // Cart Service
                .route("cart-service", r -> r
                        .path("/api/v1/users/*/cart", "/api/v1/users/*/cart/**")
                        .uri("http://localhost:8083"))

                // Order Service
                .route("order-service", r -> r
                        .path("/api/v1/users/*/orders", "/api/v1/users/*/orders/**")
                        .uri("http://localhost:8084"))

                // User Service
                .route("user-service", r -> r
                        .path("/api/v1/users", "/api/v1/users/**")
                        .uri("http://localhost:8082"))

                .build();
    }
}