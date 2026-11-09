package com.rtdi.rtdiplatform.feature.shipment.controller;

import com.rtdi.rtdiplatform.feature.shipment.model.dto.CreateShipmentRequest;
import com.rtdi.rtdiplatform.feature.shipment.model.dto.ShipmentResponse;
import com.rtdi.rtdiplatform.feature.shipment.service.ShipmentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/shipments")
public class ShipmentController {
    private final ShipmentService shipmentService;

    public ShipmentController(ShipmentService shipmentService) {
        this.shipmentService = shipmentService;
    }

    @PostMapping
    public ResponseEntity<ShipmentResponse> createShipment(@Valid @RequestBody CreateShipmentRequest createShipmentRequest) {
        ShipmentResponse response = shipmentService.createShipment(createShipmentRequest);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}
