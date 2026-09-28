package com.ritesh.user.dto.users.user;

import com.ritesh.user.enums.UserRole;
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
