package com.ritesh.order.events;


import com.ritesh.order.models.eventModels.OrderOutboxEvent;
import com.ritesh.order.repository.order.eventRepositories.OrderOutboxEventRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class OrderOutboxEventPublisher {

    private static final Logger log =
            LoggerFactory.getLogger(OrderOutboxEventPublisher.class);

    private static final String TOPIC = "order-events";
    private static final String PENDING = "PENDING";
    private static final String PUBLISHED = "PUBLISHED";

    private final OrderOutboxEventRepository outboxRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Scheduled(
            initialDelayString = "${outbox.publisher.initial-delay}",// For testing only
            fixedDelayString = "${outbox.publisher.interval}"  // For testing only
    )
    public void publishPendingEvents() {

        log.info("Outbox publisher started at {}", LocalDateTime.now());
        var pendingEvents =
                outboxRepository.findByEventStatus(PENDING);
        log.info("Found {} pending events", pendingEvents.size());

        for (OrderOutboxEvent event : pendingEvents) {
            try {
                kafkaTemplate.send(
                        TOPIC,
                        event.getOrderId().toString(),
                        event.getPayload()
                ).get();

                event.setEventStatus(PUBLISHED);
                outboxRepository.save(event);

                log.info(
                        "Published outbox event {} for order {}",
                        event.getId(),
                        event.getOrderId()
                );

            } catch (Exception e) {
                log.error(
                        "Failed to publish outbox event {} for order {}",
                        event.getId(),
                        event.getOrderId(),
                        e
                );
            }
        }
    }
}