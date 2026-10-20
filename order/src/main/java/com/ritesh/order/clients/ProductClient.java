package com.ritesh.order.clients;

import com.ritesh.order.dto.product.ProductResponseDTO;
import com.ritesh.order.dto.product.StockReserveRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class ProductClient {
    private final RestClient restClient;

    public ProductResponseDTO getProduct(Long productId, Jwt jwt) {
        ProductResponseDTO response = restClient
                .get()
                .uri("http://localhost:8081/api/v1/products/{id}", productId)
                .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + jwt.getTokenValue()
                )
                .retrieve()
                .body(ProductResponseDTO.class);

        System.out.println("PRODUCT SERVICE RESPONSE:");
        System.out.println(response);
        return response;
    }

    public void reserveProductQuantity(Long productId, Integer qty, Jwt jwt) {

        StockReserveRequestDTO stockReserveRequestDTO = new StockReserveRequestDTO();
        stockReserveRequestDTO.setQty(qty);

        restClient
                .post()
                .uri("http://localhost:8081/api/v1/products/{id}/reserve", productId)
                .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + jwt.getTokenValue()
                )
                .body(stockReserveRequestDTO)
                .retrieve()
                .body(ProductResponseDTO.class);
    }
}
