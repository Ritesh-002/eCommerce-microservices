package com.ritesh.cart.dto.cart;

import lombok.Data;

import java.util.List;

@Data
public class CartResponseDTO {
    private Long id;
    private List<CartItemResponseDTO> cartItems;
}
