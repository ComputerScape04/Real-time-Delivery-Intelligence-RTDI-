package com.rtdi.rtdiplatform.feature.event.service;

import com.rtdi.rtdiplatform.config.KafkaTopicConfig;
import com.rtdi.rtdiplatform.core.exception.ResourceNotFoundErrorMessages;
import com.rtdi.rtdiplatform.core.exception.ResourceNotFoundException;
import com.rtdi.rtdiplatform.feature.event.model.dto.ShipmentEventResponse;
import com.rtdi.rtdiplatform.feature.shipment.model.entity.Shipment;
import com.rtdi.rtdiplatform.feature.shipment.model.entity.ShipmentStatus;
import com.rtdi.rtdiplatform.feature.shipment.repository.ShipmentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ShipmentEventConsumerService {

    private static final Logger log = LoggerFactory.getLogger(ShipmentEventConsumerService.class);
    private final ShipmentRepository shipmentRepository;

    public ShipmentEventConsumerService(ShipmentRepository shipmentRepository) {
        this.shipmentRepository = shipmentRepository;
    }

    @Transactional
    @KafkaListener(topics = KafkaTopicConfig.SHIPMENT_EVENTS_TOPIC, groupId = "shipment-status-group")
    public void consumeShipmentEvent(ShipmentEventResponse shipmentEventResponse) {
        log.info("Received event for Shipment: {}, type: {}", shipmentEventResponse.shipmentId(), shipmentEventResponse.eventType());

        // query the db for the shipment
        Shipment shipment = shipmentRepository.findById(shipmentEventResponse.shipmentId())
                .orElseThrow(() -> new ResourceNotFoundException(ResourceNotFoundErrorMessages.SHIPMENT_NOT_FOUND));

        // check if shipment already reached terminal status
        if (shipment.getStatus() == ShipmentStatus.CANCELLED || shipment.getStatus() == ShipmentStatus.DELIVERED) {
            log.warn("Shipment {} is already in terminal status {}. Skipping Update", shipment.getId(), shipment.getStatus());
            return;
        }

        ShipmentStatus newStatus = switch (shipmentEventResponse.eventType()) {
            case PACKAGE_CREATED -> ShipmentStatus.CREATED;
            case PICKED_UP, IN_TRANSIT -> ShipmentStatus.IN_TRANSIT;
            case OUT_FOR_DELIVERY -> ShipmentStatus.OUT_FOR_DELIVERY;
            case CANCELLED -> ShipmentStatus.CANCELLED;
            case DELIVERED -> ShipmentStatus.DELIVERED;
        };

        shipment.setStatus(newStatus);
        shipmentRepository.save(shipment);
        log.info("Updated Shipment {} status to {}", shipment.getId(), newStatus);
    }
}
