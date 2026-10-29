package com.rtdi.rtdiplatform.feature.shipment.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity // tells hibernate that this is a relational entity
@Table(name = "shipments") // explicit binding to the schema
@Getter // boilerplate accessors
@Setter // boilerplate accessors
@NoArgsConstructor // Hibernate requires a no-arg-constructor to create entities via "reflection"
@AllArgsConstructor // for builder to work
@Builder // cleaner way of instantiating entities
public class Shipment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "tracking_number", nullable = false, unique = true, length = 32)
    private String trackingNo;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 32)
    private String status;

    @Column(name = "origin", nullable = false, length = 255)
    private String origin;

    @Column(name = "destination", nullable = false, length = 255)
    private String destination;

    @Column(name = "expected_delivery_at", nullable = false)
    private Instant expectedDeliveryAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = Instant.now();
    }
}
