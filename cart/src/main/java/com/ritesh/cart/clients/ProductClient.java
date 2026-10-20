package com.ritesh.cart.clients;

import org.springframework.security.oauth2.jwt.Jwt;
import com.ritesh.cart.dto.product.ProductResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@RequiredArgsConstructor
@Component
public class ProductClient {
    private final RestClient restClient;

    public ProductResponseDTO getProduct(Long productId, Jwt jwt) {
        return restClient.get()
//                .uri("http://product-service:8080/api/v1/products/{id}", productId)
                .uri("http://localhost:8081/api/v1/products/{id}", productId)
                .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + jwt.getTokenValue()
                )
                .retrieve()
                .body(ProductResponseDTO.class);
    }

}
