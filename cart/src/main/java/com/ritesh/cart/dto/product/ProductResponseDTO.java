package com.ritesh.cart.dto.product;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductResponseDTO {

    private Long productId;
    private String name;
    private String description;
    private BigDecimal price;
    private String sku;
    private Integer qty;
    private String brand;
    private String category;

}
