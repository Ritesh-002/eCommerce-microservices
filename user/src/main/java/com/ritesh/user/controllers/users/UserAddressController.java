package com.ritesh.user.controllers.users;

import com.ritesh.user.dto.users.userAddress.UserAddressRequestDTO;
import com.ritesh.user.dto.users.userAddress.UserAddressResponseDTO;
import com.ritesh.user.services.users.UserAddressService;
import jakarta.validation.Valid;
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

    @GetMapping
    public ResponseEntity<List<UserAddressResponseDTO>> getAddressesByUserId(
            @PathVariable Long userId) {

        return ResponseEntity.ok(
                userAddressService.fetchAddressesByUserId(userId)
        );
    }

    @PostMapping
    public ResponseEntity<UserAddressResponseDTO> createUserAddress(
            @PathVariable Long userId,
            @Valid @RequestBody UserAddressRequestDTO request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        userAddressService.addUserAddress(
                                request,
                                userId
                        )
                );
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserAddressResponseDTO> updateUserAddress(
            @PathVariable Long userId,
            @PathVariable Long id,
            @Valid @RequestBody UserAddressRequestDTO request) {

        return ResponseEntity.ok(
                userAddressService.editUserAddress(
                        request,
                        userId,
                        id
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserAddress(
            @PathVariable Long userId,
            @PathVariable Long id) {

        userAddressService.deleteUserAddress(userId, id);

        return ResponseEntity.noContent().build();
    }
}