package com.rtdi.rtdiplatform.feature.event.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "shipment_events")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class ShipmentEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    private UUID id;

    @Column(name = "event_id", nullable = false, unique = true)
    private UUID eventId;

    @Column(name = "shipment_id", nullable = false)
    private UUID shipmentId;

    @Column(name = "event_type", nullable = false, length = 64)
    private String eventType;

    @Column(name = "carrier", nullable = false, length = 64)
    private String carrier;

    @Column(name = "location", nullable = false)
    private String location;

    @Column(name = "event_timestamp", nullable = false)
    private Instant  eventTimestamp;

    @org.hibernate.annotations.JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "payload", columnDefinition = "jsonb")
    private String payload;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
    }
}
