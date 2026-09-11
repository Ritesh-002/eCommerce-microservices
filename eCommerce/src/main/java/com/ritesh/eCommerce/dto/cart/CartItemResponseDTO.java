package com.ritesh.eCommerce.dto.cart;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartItemResponseDTO {
    private Long cartId;
    private Long productId;
    private String productName;
    private BigDecimal price;
    private Integer qty;
}
