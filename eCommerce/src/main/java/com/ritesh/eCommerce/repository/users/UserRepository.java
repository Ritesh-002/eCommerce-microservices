package com.ritesh.eCommerce.repository.users;

import com.ritesh.eCommerce.models.users.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
