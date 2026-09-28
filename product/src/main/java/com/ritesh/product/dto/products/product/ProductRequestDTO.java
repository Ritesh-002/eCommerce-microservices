package com.ritesh.product.dto.products.product;

import com.ritesh.product.dto.products.productImage.ProductImageRequestDTO;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class ProductRequestDTO {

    private String name;
    private String brand;
    private String description;
    private String sku;
    private Integer qty;
    private BigDecimal price;
    private String category;

    private List<ProductImageRequestDTO> images = new ArrayList<>();
}
