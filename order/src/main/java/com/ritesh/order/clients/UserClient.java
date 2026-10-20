package com.ritesh.order.clients;

import com.ritesh.order.dto.user.UserResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class UserClient {
    private final RestClient restClient;

    public UserResponseDTO getUser(Long userId, Jwt jwt) {
        return restClient
                .get()
                .uri("http://localhost:8082/api/v1/users/{id}", userId)
                .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + jwt.getTokenValue()
                )
                .retrieve()
                .body(UserResponseDTO.class);
    }

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
