package com.ritesh.cart.dto.cart;

import lombok.Data;

@Data
public class CartRequestDTO {
    private Long productId;
    private Integer qty;
}
