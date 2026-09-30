package com.ritesh.order.dto.order;

import com.ritesh.order.enums.OrderStatus;
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
    private List<OrderItemResponseDTO> allOrderItems = new ArrayList<>();

}
