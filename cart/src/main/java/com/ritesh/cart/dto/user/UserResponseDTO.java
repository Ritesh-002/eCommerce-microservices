package com.ritesh.cart.dto.user;

import lombok.Data;

@Data
public class UserResponseDTO {
    private Long id;
    private String email;
    private String keycloakUserId;
}