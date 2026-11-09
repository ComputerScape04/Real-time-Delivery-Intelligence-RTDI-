package com.rtdi.rtdiplatform.feature.event.model.dto;

import com.rtdi.rtdiplatform.feature.event.model.entity.ShipmentEventType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public record CreateShipmentEventRequest(
    @NotNull(message = "Event ID is required")
    UUID eventId,

    @NotNull(message = "Shipment ID is required")
    UUID shipmentId,

    @NotNull(message = "Event type is required")
    ShipmentEventType eventType,

    @NotBlank(message = "Carrier is required")
    @Size(max = 64, message = "Carrier cannot exceed 64 characters")
    String carrier,

    @NotBlank(message = "Location is required")
    @Size(max = 255, message = "Carrier cannot exceed 255 characters")
    String location,

    @NotNull(message = "Event Timestamp is required")
    Instant eventTimestamp,

    String payload
) {}
