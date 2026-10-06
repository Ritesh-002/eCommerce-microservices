package com.ritesh.cart.dto.cart;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CartRequestDTO {
    @NotNull(message = "Product ID is null")
    private Long productId;
    @NotNull(message = "Missing quantity, it must be >= 1")
    @Min(1)
    private Integer qty;
}
