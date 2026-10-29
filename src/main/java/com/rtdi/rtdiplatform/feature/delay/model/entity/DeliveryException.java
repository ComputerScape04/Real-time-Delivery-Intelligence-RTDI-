package com.rtdi.rtdiplatform.feature.delay.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "delivery_exceptions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeliveryException {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    private UUID id;

    @Column(name = "shipment_id", nullable = false)
    private UUID shipmentId;

    @Enumerated(EnumType.STRING)
    @Column(name = "exception_type", nullable = false, length = 64)
    private DeliveryExceptionType exceptionType;

    @Column(name = "reason")
    private String reason;

    @Column(name = "detected_at", nullable = false)
    private Instant detectedAt;

    @Column(name = "resolved", nullable = false)
    private boolean resolved;

    @Column(name = "notified", nullable = false)
    private boolean notified;

    @PrePersist
    protected void onCreate() {
        this.detectedAt = Instant.now();
    }
}
