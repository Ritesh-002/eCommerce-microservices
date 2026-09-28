package com.ritesh.user.services.users;
import com.ritesh.user.models.users.User;
import com.ritesh.user.models.users.UserAddress;
import com.ritesh.user.repository.users.UserAddressRepository;
import com.ritesh.user.repository.users.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserAddressService {

    private final UserRepository userRepository;
    private final UserAddressRepository userAddressRepository;

    public List<UserAddress> fetchAddressesByUserId(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        return user.getUserAddresses();
    }

    public boolean addUserAddress(UserAddress address, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        address.setUser(user);
        userAddressRepository.save(address);
        return true;
    }

    public boolean editUserAddress(UserAddress address, Long userId, Long id) {
        UserAddress addressToEdit = userAddressRepository.findById(id).orElseThrow(() -> new RuntimeException("Address not found"));
        if (!addressToEdit.getUser().getId().equals(userId)) {
            throw new RuntimeException("Address does not belong to this user");
        }

        if(address.getHouseNo() != null) addressToEdit.setHouseNo(address.getHouseNo());
        if(address.getStreet() != null) addressToEdit.setStreet(address.getStreet());
        if(address.getArea() != null) addressToEdit.setArea(address.getArea());
        if(address.getCity() != null) addressToEdit.setCity(address.getCity());
        if(address.getState() != null) addressToEdit.setState(address.getState());
        if(address.getZipCode() != null) addressToEdit.setZipCode(address.getZipCode());

        userAddressRepository.save(addressToEdit);
        return true;
    }

    public boolean deleteUserAddress(Long userId, Long id) {
        UserAddress address = userAddressRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Address not found"));
        if (!address.getUser().getId().equals(userId)) {
            throw new RuntimeException("Address does not belong to this user");
        }
        userAddressRepository.delete(address);
        return true;
    }
}
