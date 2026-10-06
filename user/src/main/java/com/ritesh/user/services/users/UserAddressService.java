package com.ritesh.user.services.users;

import com.ritesh.user.dto.users.userAddress.UserAddressMapper;
import com.ritesh.user.dto.users.userAddress.UserAddressRequestDTO;
import com.ritesh.user.dto.users.userAddress.UserAddressResponseDTO;
import com.ritesh.user.exceptions.AddressNotBelongsToUser;
import com.ritesh.user.exceptions.UserAddressNotFoundException;
import com.ritesh.user.exceptions.UserNotFoundException;
import com.ritesh.user.models.users.User;
import com.ritesh.user.models.users.UserAddress;
import com.ritesh.user.repository.users.UserAddressRepository;
import com.ritesh.user.repository.users.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserAddressService {

    private final UserRepository userRepository;
    private final UserAddressRepository userAddressRepository;
    private final UserAddressMapper userAddressMapper;

    public List<UserAddressResponseDTO> fetchAddressesByUserId(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found")
                );

        return user.getUserAddresses()
                .stream()
                .map(userAddressMapper::toAddressResponseDTO)
                .toList();
    }

    @Transactional
    public UserAddressResponseDTO addUserAddress(
            UserAddressRequestDTO request,
            Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found")
                );

        UserAddress address =
                userAddressMapper.toAddressEntity(request);

        address.setUser(user);

        UserAddress savedAddress =
                userAddressRepository.save(address);

        return userAddressMapper.toAddressResponseDTO(savedAddress);
    }

    @Transactional
    public UserAddressResponseDTO editUserAddress(
            UserAddressRequestDTO request,
            Long userId,
            Long id) {

        UserAddress addressToEdit =
                userAddressRepository.findById(id)
                        .orElseThrow(() ->
                                new UserAddressNotFoundException("Address not found")
                        );

        if (!addressToEdit.getUser().getId().equals(userId)) {
            throw new AddressNotBelongsToUser(
                    "Address does not belong to this user"
            );
        }

        if (request.getHouseNo() != null) {
            addressToEdit.setHouseNo(request.getHouseNo());
        }

        if (request.getStreet() != null) {
            addressToEdit.setStreet(request.getStreet());
        }

        if (request.getArea() != null) {
            addressToEdit.setArea(request.getArea());
        }

        if (request.getCity() != null) {
            addressToEdit.setCity(request.getCity());
        }

        if (request.getState() != null) {
            addressToEdit.setState(request.getState());
        }

        if (request.getZipCode() != null) {
            addressToEdit.setZipCode(request.getZipCode());
        }

        UserAddress updatedAddress =
                userAddressRepository.save(addressToEdit);

        return userAddressMapper.toAddressResponseDTO(updatedAddress);
    }

    @Transactional
    public void deleteUserAddress(Long userId, Long id) {

        UserAddress address =
                userAddressRepository.findById(id)
                        .orElseThrow(() ->
                                new UserAddressNotFoundException("Address not found")
                        );

        if (!address.getUser().getId().equals(userId)) {
            throw new AddressNotBelongsToUser(
                    "Address does not belong to this user"
            );
        }

        userAddressRepository.delete(address);
    }
}