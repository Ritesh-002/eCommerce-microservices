package com.ritesh.order.models.eventModels;

import com.ritesh.order.enums.OrderEvents;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "outbox_events")
@Getter
@NoArgsConstructor
@Data
public class OrderOutboxEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long orderId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderEvents eventType;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String payload;

    @Column(nullable = false, length = 20)
    private String eventStatus = "PENDING";

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public OrderOutboxEvent(
            Long orderId,
            OrderEvents eventType,
            String payload
    ) {
        this.orderId = orderId;
        this.eventType = eventType;
        this.payload = payload;
        this.eventStatus = "PENDING";
        this.createdAt = LocalDateTime.now();
    }
}
