package com.ritesh.user.dto.users.userAddress;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserAddressRequestDTO {
    @NotNull(message = "House no. is required!")
    private Integer houseNo;
    @NotNull(message = "Street is required!")
    private String street;
    @NotNull(message = "Area is required!")
    private String area;
    @NotNull(message = "City is required!")
    private String city;
    @NotNull(message = "State is required!")
    private String state;
    @NotNull(message = "Zipcode is required!")
    private Integer zipCode;
}
