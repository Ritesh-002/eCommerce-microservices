package com.ritesh.cart.clients;

import com.ritesh.cart.dto.product.ProductResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@RequiredArgsConstructor
@Component
public class ProductClient {
    private final RestClient restClient;

    public ProductResponseDTO getProduct(Long productId) {
        return restClient.get()
                .uri("http://localhost:8081/api/v1/products/{id}", productId)
                .retrieve()
                .body(ProductResponseDTO.class);
    }

}
