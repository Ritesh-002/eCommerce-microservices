package com.ritesh.cart.repository.cart;

import com.ritesh.cart.models.cart.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
    public Cart findCartByUserId(Long userId);
}
