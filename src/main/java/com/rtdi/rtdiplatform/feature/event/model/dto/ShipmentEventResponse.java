package com.rtdi.rtdiplatform.feature.event.model.dto;

import com.rtdi.rtdiplatform.feature.event.model.entity.ShipmentEvent;
import com.rtdi.rtdiplatform.feature.event.model.entity.ShipmentEventType;

import java.time.Instant;
import java.util.UUID;

public record ShipmentEventResponse(
        UUID id,
        UUID eventId,
        UUID shipmentId,
        ShipmentEventType eventType,
        String carrier,
        String location,
        Instant eventTimestamp,
        String payload,
        Instant createdAt
) {
    public static ShipmentEventResponse from(ShipmentEvent shipmentEvent) {
        return new ShipmentEventResponse(
                shipmentEvent.getId(),
                shipmentEvent.getEventId(),
                shipmentEvent.getShipmentId(),
                shipmentEvent.getEventType(),
                shipmentEvent.getCarrier(),
                shipmentEvent.getLocation(),
                shipmentEvent.getEventTimestamp(),
                shipmentEvent.getPayload(),
                shipmentEvent.getCreatedAt()
        );
    }
}
