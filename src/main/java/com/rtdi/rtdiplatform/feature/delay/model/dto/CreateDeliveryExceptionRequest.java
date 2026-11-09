package com.rtdi.rtdiplatform.feature.delay.model.dto;

import com.rtdi.rtdiplatform.feature.delay.model.entity.DeliveryExceptionType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record CreateDeliveryExceptionRequest(
        @NotNull(message = "Shipment ID is required")
        UUID shipmentId,

        @NotNull(message = "Exception Type is required")
        DeliveryExceptionType exceptionType,

        @Size(max = 500, message = "Reason cannot exceed 500 characters")
        String reason
) {}
