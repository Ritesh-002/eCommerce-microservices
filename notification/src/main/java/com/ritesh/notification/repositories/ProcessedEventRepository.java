package com.ritesh.notification.repositories;

import com.ritesh.notification.models.ProcessedEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProcessedEventRepository extends JpaRepository<ProcessedEvent, Long> {
}
