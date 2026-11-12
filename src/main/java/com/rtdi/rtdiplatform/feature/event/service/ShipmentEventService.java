package com.rtdi.rtdiplatform.feature.event.service;

import com.rtdi.rtdiplatform.core.exception.ResourceNotFoundErrorMessages;
import com.rtdi.rtdiplatform.core.exception.ResourceNotFoundException;
import com.rtdi.rtdiplatform.feature.event.model.dto.CreateShipmentEventRequest;
import com.rtdi.rtdiplatform.feature.event.model.dto.ShipmentEventResponse;
import com.rtdi.rtdiplatform.feature.event.model.entity.ShipmentEvent;
import com.rtdi.rtdiplatform.feature.event.repository.ShipmentEventRepository;
import com.rtdi.rtdiplatform.feature.shipment.model.entity.Shipment;
import com.rtdi.rtdiplatform.feature.shipment.repository.ShipmentRepository;
import org.springframework.stereotype.Service;

@Service
public class ShipmentEventService {
    private final ShipmentEventRepository shipmentEventRepository;
    private final ShipmentRepository shipmentRepository;
    private final ShipmentEventProducerService shipmentEventProducerService;

    public ShipmentEventService(
            ShipmentEventRepository shipmentEventRepository,
            ShipmentRepository shipmentRepository,
            ShipmentEventProducerService shipmentEventProducerService
    ) {
        this.shipmentEventRepository = shipmentEventRepository;
        this.shipmentRepository = shipmentRepository;
        this.shipmentEventProducerService = shipmentEventProducerService;
    }

    public ShipmentEventResponse recordEvent(CreateShipmentEventRequest request) {
        // check if the shipment exists
        Shipment shipment = shipmentRepository.findById(request.shipmentId())
                .orElseThrow(() -> new ResourceNotFoundException(ResourceNotFoundErrorMessages.SHIPMENT_NOT_FOUND));

        // create shipment event
        ShipmentEvent event = ShipmentEvent.builder()
                .eventId(request.eventId())
                .eventTimestamp(request.eventTimestamp())
                .eventType(request.eventType())
                .carrier(request.carrier())
                .location(request.location())
                .shipmentId(shipment.getId())
                .payload(request.payload())
                .build();

        // save the event to DB
        ShipmentEvent savedEvent = shipmentEventRepository.save(event);

        // convert the above to event response and publish to kafka
        ShipmentEventResponse eventResponse = ShipmentEventResponse.from(savedEvent);
        shipmentEventProducerService.sendShipmentEvent(shipment.getTrackingNo(), eventResponse);

        return eventResponse;
    }
}
