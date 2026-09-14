package com.ritesh.eCommerce.repository.cart;

import com.ritesh.eCommerce.models.cart.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

}
