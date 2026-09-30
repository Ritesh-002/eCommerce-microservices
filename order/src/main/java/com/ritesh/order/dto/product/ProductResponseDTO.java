package com.ritesh.order.dto.product;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductResponseDTO {
    private Long productId;
    private String productName;
    private BigDecimal price;
    private Integer qty;
}
