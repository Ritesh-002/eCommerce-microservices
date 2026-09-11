package com.ritesh.eCommerce.dto.cart;

import com.ritesh.eCommerce.models.cart.Cart;
import com.ritesh.eCommerce.models.users.User;
import com.ritesh.eCommerce.services.users.UserService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CartMapper {

    private final UserService userService;

    public CartResponseDTO toCartResponseDTO(Cart cart) {
        CartResponseDTO cartResponseDTO = new CartResponseDTO();
        cartResponseDTO.setId(cart.getId());
        cartResponseDTO.setUser(cart.getUser());
        cartResponseDTO.setCartProducts(cart.getCartProducts());

        return cartResponseDTO;
    }

    public Cart toCartEntity(CartRequestDTO cartRequestDTO) {

        Cart cart = new Cart();
        User user = userService.getUser(cartRequestDTO.getUserId()).orElseThrow(() -> new RuntimeException("No user found!"));

        cart.setUser(user);
        return cart;

    }
}
