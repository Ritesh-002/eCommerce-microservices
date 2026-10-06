package com.ritesh.product.dto.products.product;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReserveStockRequestDTO {

    @NotNull(message = "Quantity is required!")
    @Min(value = 1, message = "Quantity must be positive")
    private Integer qty;
}
