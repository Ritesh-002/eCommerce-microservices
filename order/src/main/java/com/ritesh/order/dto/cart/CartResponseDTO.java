package com.ritesh.order.dto.cart;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CartResponseDTO {
    private Long cartId;
    private List<CartItemResponseDTO> cartItems = new ArrayList<>();
}
