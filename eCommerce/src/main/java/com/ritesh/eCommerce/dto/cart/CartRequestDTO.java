package com.ritesh.eCommerce.dto.cart;

import com.ritesh.eCommerce.models.products.Product;
import lombok.Data;

@Data
public class CartRequestDTO {
    private Long userId;
    private Product product;
    private Integer qty;
}
