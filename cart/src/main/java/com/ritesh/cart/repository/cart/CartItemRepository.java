package com.ritesh.cart.repository.cart;

import com.ritesh.cart.models.cart.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

}
