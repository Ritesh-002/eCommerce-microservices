package com.ritesh.user.repository.users;

import com.ritesh.user.models.users.UserAddress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAddressRepository extends JpaRepository<UserAddress, Long> {
}
