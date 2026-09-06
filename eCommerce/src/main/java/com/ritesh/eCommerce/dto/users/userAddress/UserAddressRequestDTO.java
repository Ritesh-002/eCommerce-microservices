package com.ritesh.eCommerce.dto.users.userAddress;

import lombok.Data;

@Data
public class UserAddressRequestDTO {
    private Integer houseNo;
    private String street;
    private String area;
    private String city;
    private String state;
    private Integer zipCode;
}
