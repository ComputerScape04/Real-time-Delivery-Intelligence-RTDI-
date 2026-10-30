package com.rtdi.rtdiplatform.feature.shipment.model.dto;

import com.rtdi.rtdiplatform.feature.shipment.model.entity.Shipment;
import com.rtdi.rtdiplatform.feature.shipment.model.entity.ShipmentStatus;

import java.time.Instant;
import java.util.UUID;

public record ShipmentResponse(
   UUID id,
   String trackingNo,
   ShipmentStatus status,
   String origin,
   String destination,
   Instant expectedDeliveryAt,
   Instant createdAt,
   Instant updatedAt
) {
    // function to map Shipment to ShipmentResponse
    public static ShipmentResponse from(Shipment shipment) {
        return new ShipmentResponse(
                shipment.getId(),
                shipment.getTrackingNo(),
                shipment.getStatus(),
                shipment.getOrigin(),
                shipment.getDestination(),
                shipment.getExpectedDeliveryAt(),
                shipment.getCreatedAt(),
                shipment.getUpdatedAt()
        );
    }
}
