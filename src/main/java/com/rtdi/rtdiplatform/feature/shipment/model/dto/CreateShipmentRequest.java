package com.rtdi.rtdiplatform.feature.shipment.model.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.Instant;

public record CreateShipmentRequest(
        @NotBlank(message = "Tracking Number is required")
        @Size(max = 64, message = "Tracking Number must not exceed 64 characters")
        String trackingNo,

        @NotBlank(message = "Origin is required")
        @Size(max = 255)
        String origin,

        @NotBlank(message = "Destination is required")
        @Size(max = 255)
        String destination,

        @NotNull(message = "Expected Delivery Date is required")
        @Future(message = "Expected Delivery Date must be in the future")
        Instant expectedDeliveryAt
) {}
