package com.ritesh.user.services.users;

import com.ritesh.user.dto.users.user.UserMappers;
import com.ritesh.user.dto.users.user.UserRequestDTO;
import com.ritesh.user.dto.users.user.UserResponseDTO;
import com.ritesh.user.exceptions.UserNotFoundException;
import com.ritesh.user.models.users.User;
import com.ritesh.user.repository.users.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMappers userMappers;

    public List<UserResponseDTO> fetchAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(userMappers::toUserResponseDTO)
                .toList();
    }

    public UserResponseDTO getUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new UserNotFoundException("User with id " + id + " not found")
                );

        return userMappers.toUserResponseDTO(user);
    }

    @Transactional
    public UserResponseDTO addUser(UserRequestDTO request) {

        User user = userMappers.toUserEntity(request);

        User savedUser = userRepository.save(user);

        return userMappers.toUserResponseDTO(savedUser);
    }

    @Transactional
    public UserResponseDTO editUser(Long id, UserRequestDTO request) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new UserNotFoundException("User with id " + id + " not found")
                );

        if (request.getFirstName() != null) {
            user.setFirstName(request.getFirstName());
        }

        if (request.getLastName() != null) {
            user.setLastName(request.getLastName());
        }

        User updatedUser = userRepository.save(user);

        return userMappers.toUserResponseDTO(updatedUser);
    }


    @Transactional
    public UserResponseDTO createMyProfile(
            UserRequestDTO request,
            Jwt jwt) {

        String keycloakUserId = jwt.getSubject();
        String email = jwt.getClaimAsString("email");

        if (email == null || email.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Email is missing from the access token"
            );
        }

        // Make the operation idempotent.
        var existingUser =
                userRepository.findByKeycloakUserId(keycloakUserId);

        if (existingUser.isPresent()) {
            return userMappers.toUserResponseDTO(existingUser.get());
        }

        // Do not silently link an existing account by email.
        if (userRepository.findByEmail(email).isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "A profile with this email already exists. Account linking is required."
            );
        }

        User user = new User();
        user.setKeycloakUserId(keycloakUserId);
        user.setEmail(email);
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());

        // Retain your entity's default CUSTOMER role.
        User savedUser = userRepository.save(user);

        return userMappers.toUserResponseDTO(savedUser);
    }

}