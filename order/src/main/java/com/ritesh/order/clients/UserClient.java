package com.ritesh.order.clients;

import com.ritesh.order.dto.user.UserResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class UserClient {
    private final RestClient restClient;

    public UserResponseDTO getUser(Long userId) {
        return restClient
                .get()
                .uri("http://localhost:8082/api/v1/users/{userId}", userId)
                .retrieve()
                .body(UserResponseDTO.class);
    }

}
