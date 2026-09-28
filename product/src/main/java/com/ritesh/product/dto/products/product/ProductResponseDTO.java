package com.ritesh.product.dto.products.product;

import com.ritesh.product.dto.products.productImage.ProductImageResponseDTO;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class ProductResponseDTO {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private String sku;
    private Integer qty;
    private String brand;
    private String category;

    private List<ProductImageResponseDTO> images = new ArrayList<>();
}
