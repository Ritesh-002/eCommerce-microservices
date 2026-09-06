package com.ritesh.eCommerce.dto.users.userAddress;


import com.ritesh.eCommerce.models.users.UserAddress;
import org.springframework.stereotype.Component;

@Component
public class UserAddressMapper {

    public UserAddressResponseDTO toAddressResponseDTO(UserAddress userAddress) {
        UserAddressResponseDTO userAddressResponseDTO = new UserAddressResponseDTO();
        userAddressResponseDTO.setId(userAddress.getId());
        userAddressResponseDTO.setHouseNo(userAddress.getHouseNo());
        userAddressResponseDTO.setStreet(userAddress.getStreet());
        userAddressResponseDTO.setArea(userAddress.getArea());
        userAddressResponseDTO.setCity(userAddress.getCity());
        userAddressResponseDTO.setState(userAddress.getState());
        userAddressResponseDTO.setZipCode(userAddress.getZipCode());

        return userAddressResponseDTO;
    }

    public UserAddress toAddressEntity(UserAddressRequestDTO userAddressRequestDTO) {
        UserAddress userAddress = new UserAddress();
        userAddress.setHouseNo(userAddressRequestDTO.getHouseNo());
        userAddress.setStreet(userAddressRequestDTO.getStreet());
        userAddress.setArea(userAddressRequestDTO.getArea());
        userAddress.setCity(userAddressRequestDTO.getCity());
        userAddress.setState(userAddressRequestDTO.getState());
        userAddress.setZipCode(userAddressRequestDTO.getZipCode());
        return userAddress;
    }
}
