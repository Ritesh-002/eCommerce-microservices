package com.ritesh.user.controllers.users;

import com.ritesh.user.dto.users.user.UserMappers;
import com.ritesh.user.dto.users.user.UserRequestDTO;
import com.ritesh.user.dto.users.user.UserResponseDTO;
import com.ritesh.user.models.users.User;
import com.ritesh.user.services.users.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;
    private final UserMappers userMappers;

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<User> allUsers = userService.fetchAllUsers();
        List<UserResponseDTO> allUserResponseDTO = allUsers.stream().map(userMappers::toUserResponseDTO).toList();
        return new ResponseEntity<>(allUserResponseDTO, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUser(@PathVariable Long id) {
        return userService.getUser(id)
                .map(u -> new ResponseEntity<>(userMappers.toUserResponseDTO(u), HttpStatus.OK))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody UserRequestDTO userRequestDTO) {
        User user = userMappers.toUserEntity(userRequestDTO);
        return userService.addUser(user) ?
                ResponseEntity.ok("User added successfully") :
                new ResponseEntity<>("Operation failed! Please try again", HttpStatus.SERVICE_UNAVAILABLE);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@RequestBody UserRequestDTO userRequestDTO, @PathVariable Long id) {
        User user = userMappers.toUserEntity(userRequestDTO);
        return userService.editUser(user, id) ? ResponseEntity.ok("User updated successfully") : new ResponseEntity<>("User failed to udpate", HttpStatus.SERVICE_UNAVAILABLE);
    }
}
