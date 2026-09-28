package com.ritesh.user.dto.users.user;


import com.ritesh.user.models.users.User;
import org.springframework.stereotype.Component;

@Component
public class UserMappers {

    public UserResponseDTO toUserResponseDTO(User user) {
        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setId(user.getId());
        userResponseDTO.setFirstName(user.getFirstName());
        userResponseDTO.setLastName(user.getLastName());
        userResponseDTO.setEmail(user.getEmail());
        userResponseDTO.setContact(user.getContact());
        userResponseDTO.setRole(user.getRole());
        return userResponseDTO;
    }

    public User toUserEntity(UserRequestDTO userRequestDTO) {
        User user = new User();
        user.setFirstName(userRequestDTO.getFirstName());
        user.setLastName(userRequestDTO.getLastName());
        user.setContact(userRequestDTO.getContact());
        user.setEmail(userRequestDTO.getEmail());
        return user;
    }
}
