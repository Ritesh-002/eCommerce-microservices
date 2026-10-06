package com.ritesh.user.dto.users.user;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserRequestDTO {

    @NotNull(message = "First name is required!")
    private String firstName;

    @NotNull(message = "Last name is required!")
    private String lastName;

    @NotNull(message = "Email name is required!")
    private String email;

    @NotNull(message = "Phone is required!")
    private String contact;
}
