
package com.ritesh.order.events;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderCreatedEvent(
        Long orderId,
        Long userId,
        BigDecimal totalAmount,
        LocalDateTime placedOn
) {
}