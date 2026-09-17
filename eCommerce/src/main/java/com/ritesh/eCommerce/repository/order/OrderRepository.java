package com.ritesh.eCommerce.repository.order;

import com.ritesh.eCommerce.models.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    public List<Order> findOrdersByUserId(Long userId);

}
