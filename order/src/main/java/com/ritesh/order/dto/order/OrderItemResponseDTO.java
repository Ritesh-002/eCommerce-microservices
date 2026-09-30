package com.ritesh.order.dto.order;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemResponseDTO {
    private Long orderItemId;
    private Long productId;
    private String productName;
    private BigDecimal pricePerUnit;
    private Integer qty;
    private BigDecimal subTotal;
}
