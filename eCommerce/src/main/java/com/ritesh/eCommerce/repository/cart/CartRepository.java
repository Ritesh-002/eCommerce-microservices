package com.ritesh.eCommerce.repository.cart;

import com.ritesh.eCommerce.models.cart.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    public Cart findCartByUserId(Long userId);
}
