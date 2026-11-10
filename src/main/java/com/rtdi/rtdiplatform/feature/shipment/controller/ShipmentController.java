package com.rtdi.rtdiplatform.feature.shipment.controller;

import com.rtdi.rtdiplatform.feature.shipment.model.dto.CreateShipmentRequest;
import com.rtdi.rtdiplatform.feature.shipment.model.dto.ShipmentResponse;
import com.rtdi.rtdiplatform.feature.shipment.model.dto.UpdateShipmentRequest;
import com.rtdi.rtdiplatform.feature.shipment.service.ShipmentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping
    public ResponseEntity<ShipmentResponse>  updateShipment(@Valid @RequestBody UpdateShipmentRequest updateShipmentRequest) {
        ShipmentResponse response = shipmentService.updateShipment(updateShipmentRequest);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @DeleteMapping("/{trackingNumber}")
    public ResponseEntity<String> deleteShipment(@Valid @PathVariable String trackingNumber) {
        shipmentService.deleteShipment(trackingNumber);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Shipment has been deleted successfully");
    }

    @GetMapping("/{trackingNumber}")
    public ResponseEntity<ShipmentResponse> getShipment(@Valid @PathVariable String trackingNumber) {
        ShipmentResponse response = shipmentService.getShipmentByTrackingNo(trackingNumber);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}
