package com.ritesh.user.repository.users;

import com.ritesh.user.models.users.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
