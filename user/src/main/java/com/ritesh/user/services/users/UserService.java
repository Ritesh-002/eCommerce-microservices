package com.ritesh.user.services.users;

import com.ritesh.user.models.users.User;
import com.ritesh.user.repository.users.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
//    List<User> allUsers = new ArrayList<>();
    private final UserRepository userRepository;

    public List<User> fetchAllUsers() {
        // return allUsers;

        return userRepository.findAll();
    }

    public Optional<User> getUser(Long id) {
//        return allUsers.stream().filter(u -> u.getId().equals(id)).findFirst();

        return userRepository.findById(id);
    }


    public boolean addUser(User user) {
//        if(!allUsers.isEmpty()) user.setId(allUsers.getLast().getId() + 1);
//        else user.setId(1L);
//        return allUsers.add(user);
        userRepository.save(user);
        return true;
    }

    public boolean editUser(User userToUpdate, Long id) {

//        User user = allUsers.stream().filter(u -> u.getId().equals(id)).findFirst().orElseGet(null);
//        if(user != null) {
//            if(userToUpdate.getFirstName() != null) user.setFirstName(userToUpdate.getFirstName());
//            if(userToUpdate.getLastName() != null) user.setLastName(userToUpdate.getLastName());
//            return true;
//        }
//        return false;

        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));

        if(userToUpdate.getFirstName() != null) user.setFirstName(userToUpdate.getFirstName());
        if(userToUpdate.getLastName() != null) user.setLastName(userToUpdate.getLastName());

        userRepository.save(user);
        return true;
    }
}
