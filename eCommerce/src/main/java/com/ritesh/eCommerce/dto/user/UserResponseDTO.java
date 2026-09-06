package com.ritesh.eCommerce.dto.user;

import com.ritesh.eCommerce.enums.UserRole;
import lombok.Data;

@Data
public class UserResponseDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String contact;
    private UserRole role;
}
