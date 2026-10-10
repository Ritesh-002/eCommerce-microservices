package com.ritesh.order.repository.order.eventRepositories;

import com.ritesh.order.models.eventModels.OrderOutboxEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderOutboxEventRepository extends JpaRepository<OrderOutboxEvent, Long> {
    public List<OrderOutboxEvent> findByEventStatus(String status);
}
