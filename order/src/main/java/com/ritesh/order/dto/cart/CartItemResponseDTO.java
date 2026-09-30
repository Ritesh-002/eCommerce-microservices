package com.ritesh.order.dto.cart;

import lombok.Data;

@Data
public class CartItemResponseDTO {
    private Long productId;
    private Integer qty;
}
