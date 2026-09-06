package com.ritesh.eCommerce.repository;

import com.ritesh.eCommerce.models.UserAddress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAddressRepository extends JpaRepository<UserAddress, Long> {
}
