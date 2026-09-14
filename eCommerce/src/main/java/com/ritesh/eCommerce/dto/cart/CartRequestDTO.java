package com.ritesh.eCommerce.dto.cart;

import lombok.Data;

@Data
public class CartRequestDTO {
    private Long productId;
    private Integer qty;
}
