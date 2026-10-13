package com.ritesh.notification.events;

import com.ritesh.notification.models.ProcessedEvent;
import com.ritesh.notification.repositories.ProcessedEventRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class OrderEventConsumer {

    private final ProcessedEventRepository processedEventRepository;

    private static final Logger log =
            LoggerFactory.getLogger(OrderEventConsumer.class);

    @KafkaListener(topics = "order-events")
    @Transactional
    public void consumeOrderCreated(OrderCreatedEvent event) {

        if (processedEventRepository.existsById(event.eventId())) {
            log.info("Duplicate event {} detected. Skipping.", event.eventId());
            return;
        }

        log.info(
                "Order {} placed successfully for user {}. Total amount: {}",
                event.orderId(),
                event.userId(),
                event.totalAmount()
        );

        processedEventRepository.save(
                new ProcessedEvent(event.eventId())
        );

        log.info("Event {} marked as processed.", event.eventId());
    }
}