package com.ritesh.cart.dto.cart;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartItemResponseDTO {
    private Long cartItemId;
    private Long productId;
    private String productName;
    private BigDecimal price;
    private Integer qty;
}
