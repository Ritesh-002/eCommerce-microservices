package com.ritesh.eCommerce.dto.userAddress;

import com.ritesh.eCommerce.enums.UserRole;
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
