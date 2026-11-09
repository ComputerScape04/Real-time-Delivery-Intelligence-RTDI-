package com.rtdi.rtdiplatform.feature.delay.model.dto;

import com.rtdi.rtdiplatform.feature.delay.model.entity.DeliveryException;
import com.rtdi.rtdiplatform.feature.delay.model.entity.DeliveryExceptionType;

import java.time.Instant;
import java.util.UUID;

public record DeliveryExceptionResponse(
        UUID id,
        UUID shipmentId,
        DeliveryExceptionType exceptionType,
        String reason,
        Instant detectedAt,
        boolean resolved,
        boolean notified
) {

    public static DeliveryExceptionResponse from(DeliveryException deliveryException) {
        return new DeliveryExceptionResponse(
                deliveryException.getId(),
                deliveryException.getShipmentId(),
                deliveryException.getExceptionType(),
                deliveryException.getReason(),
                deliveryException.getDetectedAt(),
                deliveryException.isResolved(),
                deliveryException.isNotified()
        );
    }
}
