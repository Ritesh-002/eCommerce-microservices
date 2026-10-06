package com.ritesh.user.services.users;

import com.ritesh.user.dto.users.user.UserMappers;
import com.ritesh.user.dto.users.user.UserRequestDTO;
import com.ritesh.user.dto.users.user.UserResponseDTO;
import com.ritesh.user.models.users.User;
import com.ritesh.user.repository.users.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
                        new RuntimeException("User with id " + id + " not found")
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
                        new RuntimeException("User with id " + id + " not found")
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
}