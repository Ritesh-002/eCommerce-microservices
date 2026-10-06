package com.ritesh.cart.dto.cart;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateCartItemRequestDTO {
    @NotNull(message = "Quantity is required and it must be >= 1")
    @Min(1)
    private Integer qty;
}
