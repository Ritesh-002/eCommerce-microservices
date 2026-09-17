package com.ritesh.eCommerce.repository.order;

import com.ritesh.eCommerce.models.order.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
