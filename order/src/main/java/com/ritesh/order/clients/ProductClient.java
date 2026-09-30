package com.ritesh.order.clients;

import com.ritesh.order.dto.product.ProductResponseDTO;
import com.ritesh.order.dto.product.StockReserveRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class ProductClient {
    private final RestClient restClient;

    public ProductResponseDTO getProduct(Long productId) {
        return restClient
                .get()
                .uri("http://localhost:8081/api/v1/products/{productId}", productId)
                .retrieve()
                .body(ProductResponseDTO.class);
    }

    public ProductResponseDTO reserveProductQuantity(Long productId, Integer qty) {

        StockReserveRequestDTO stockReserveRequestDTO = new StockReserveRequestDTO();
        stockReserveRequestDTO.setQty(qty);

        return restClient
                .post()
                .uri("http://localhost:8081/api/v1/products/{productId}/reserve", productId)
                .body(stockReserveRequestDTO)
                .retrieve()
                .body(ProductResponseDTO.class);
    }
}
