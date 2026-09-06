package com.ritesh.eCommerce.repository;

import com.ritesh.eCommerce.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
