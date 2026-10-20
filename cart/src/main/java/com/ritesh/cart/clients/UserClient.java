package com.ritesh.cart.clients;

import com.ritesh.cart.dto.user.UserResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@RequiredArgsConstructor
@Component
public class UserClient {
    private final RestClient restClient;

    public String getUserKeycloakId(Long userId, Jwt jwt) {
        return restClient.get()
                .uri("http://localhost:8082/api/v1/users/{id}/keycloak-id", userId)
                .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + jwt.getTokenValue()
                )
                .retrieve()
                .body(String.class);
    }
}
