package com.ritesh.eCommerce.dto.users.user;

import lombok.Data;

@Data
public class UserRequestDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String contact;
}
