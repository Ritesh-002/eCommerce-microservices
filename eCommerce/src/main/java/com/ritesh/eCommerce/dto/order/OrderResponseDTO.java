package com.ritesh.eCommerce.dto.order;

import com.ritesh.eCommerce.enums.OrderStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class OrderResponseDTO {

    private Long orderId;
    private BigDecimal totalAmount;
    private OrderStatus status;
    private LocalDateTime placedOn;
    private List<OrderItemResponseDTO> allOrderItemsResponse = new ArrayList<>();

}
