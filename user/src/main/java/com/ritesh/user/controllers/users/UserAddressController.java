package com.ritesh.user.controllers.users;
import com.ritesh.user.dto.users.userAddress.UserAddressMapper;
import com.ritesh.user.dto.users.userAddress.UserAddressRequestDTO;
import com.ritesh.user.dto.users.userAddress.UserAddressResponseDTO;
import com.ritesh.user.services.users.UserAddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users/{userId}/addresses")
public class UserAddressController {

    private final UserAddressService userAddressService;
    private final UserAddressMapper userAddressMapper;

    @GetMapping
    public ResponseEntity<List<UserAddressResponseDTO>> getAddressesByUserId(@PathVariable Long userId) {
        return new ResponseEntity<>(userAddressService.fetchAddressesByUserId(userId).stream().map(userAddressMapper::toAddressResponseDTO).toList(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> createUserAddress(@RequestBody UserAddressRequestDTO userAddressRequestDTO, @PathVariable Long userId) {
        return userAddressService.addUserAddress(userAddressMapper.toAddressEntity(userAddressRequestDTO), userId) ?
                ResponseEntity.ok("Address added successfully") :
                new ResponseEntity<>("Operation failed! Please try again", HttpStatus.SERVICE_UNAVAILABLE);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateUserAddress(@RequestBody UserAddressRequestDTO userAddressRequestDTO, @PathVariable Long userId, @PathVariable Long id) {
        return userAddressService.editUserAddress(userAddressMapper.toAddressEntity(userAddressRequestDTO), userId, id) ? ResponseEntity.ok("Address updated successfully") : new ResponseEntity<>("User failed to udpate the address", HttpStatus.SERVICE_UNAVAILABLE);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUserAddress(@PathVariable Long userId, @PathVariable Long id) {
        return userAddressService.deleteUserAddress(userId, id) ? ResponseEntity.ok("Address deleted successfully") : new ResponseEntity<>("Address failed to delete", HttpStatus.SERVICE_UNAVAILABLE);
    }

}
