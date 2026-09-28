package com.ritesh.user.dto.users.userAddress;

import lombok.Data;

@Data
public class UserAddressResponseDTO {
    private Long id;
    private Integer houseNo;
    private String street;
    private String area;
    private String city;
    private String state;
    private Integer zipCode;
}
