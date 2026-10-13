package com.ritesh.notification.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "processed_events")
@Getter
@NoArgsConstructor
public class ProcessedEvent {

    @Id
    @Column(nullable = false, updatable = false)
    private Long eventId;

    @Column(nullable = false, updatable = false)
    private LocalDateTime processedAt;

    public ProcessedEvent(Long eventId) {
        this.eventId = eventId;
        this.processedAt = LocalDateTime.now();
    }
}