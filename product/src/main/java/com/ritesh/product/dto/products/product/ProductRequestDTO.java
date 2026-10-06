package com.ritesh.product.dto.products.product;

import com.ritesh.product.dto.products.productImage.ProductImageRequestDTO;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class ProductRequestDTO {

    @NotBlank(message = "Name is required!")
    private String name;

    @NotBlank(message = "Brand is required!")
    private String brand;

    @NotBlank(message = "Description is required!")
    private String description;

    @NotBlank(message = "Sku is required!")
    private String sku;

    @NotNull(message = "Quantity is required!")
    @Min(value = 1, message = "Quantity cannot be negative")
    private Integer qty;

    @NotNull(message = "Price is required!")
    @Positive(message = "Price must be greater than 0")
    private BigDecimal price;
    private String category;

    private List<ProductImageRequestDTO> images = new ArrayList<>();
}
