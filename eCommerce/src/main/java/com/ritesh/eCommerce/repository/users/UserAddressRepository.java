package com.ritesh.eCommerce.repository.users;

import com.ritesh.eCommerce.models.users.UserAddress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAddressRepository extends JpaRepository<UserAddress, Long> {
}
