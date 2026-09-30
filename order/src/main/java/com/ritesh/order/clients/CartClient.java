package com.ritesh.order.clients;

import com.ritesh.order.dto.cart.CartResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class CartClient {
    private final RestClient restClient;

    public CartResponseDTO getCart(Long userId) {
        return restClient.get()
                .uri("http://localhost:8083/api/v1/users/{userId}/cart", userId)
                .retrieve()
                .body(CartResponseDTO.class);
    }

    public void clearCart(Long userId) {
        restClient.delete()
                .uri("http://localhost:8083/api/v1/users/{userId}/cart", userId)
                .retrieve()
                .toBodilessEntity();
    }
}
