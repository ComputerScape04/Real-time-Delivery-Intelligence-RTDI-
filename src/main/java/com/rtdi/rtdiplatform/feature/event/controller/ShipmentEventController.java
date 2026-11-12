package com.rtdi.rtdiplatform.feature.event.controller;

import com.rtdi.rtdiplatform.feature.event.model.dto.CreateShipmentEventRequest;
import com.rtdi.rtdiplatform.feature.event.model.dto.ShipmentEventResponse;
import com.rtdi.rtdiplatform.feature.event.service.ShipmentEventService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/events")
public class ShipmentEventController {
    private final ShipmentEventService shipmentEventService;

    public ShipmentEventController(ShipmentEventService shipmentEventService) {
        this.shipmentEventService = shipmentEventService;
    }

    @PostMapping
    public ResponseEntity<ShipmentEventResponse> createShipmentEvent(@Valid @RequestBody CreateShipmentEventRequest createShipmentEventRequest) {
        ShipmentEventResponse createdShipmentEvent = shipmentEventService.recordEvent(createShipmentEventRequest);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdShipmentEvent);
    }
}
