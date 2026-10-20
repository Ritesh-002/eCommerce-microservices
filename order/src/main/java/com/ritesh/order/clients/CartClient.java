package com.ritesh.order.clients;

import com.ritesh.order.dto.cart.CartResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class CartClient {
    private final RestClient restClient;

    public CartResponseDTO getCart(Long userId, Jwt jwt) {
        return restClient.get()
                .uri("http://localhost:8083/api/v1/users/{userId}/cart", userId)
                .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + jwt.getTokenValue()
                )
                .retrieve()
                .body(CartResponseDTO.class);
    }

    public void clearCart(Long userId, Jwt jwt) {
        restClient.delete()
                .uri("http://localhost:8083/api/v1/users/{userId}/cart", userId)
                .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + jwt.getTokenValue()
                )
                .retrieve()
                .toBodilessEntity();
    }
}
