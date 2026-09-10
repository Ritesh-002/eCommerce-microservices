package com.ritesh.eCommerce.repository.cart;

import com.ritesh.eCommerce.models.cart.UserCart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<UserCart, Long> {
}
