package com.rtdi.rtdiplatform.feature.event.service;

import com.rtdi.rtdiplatform.config.KafkaTopicConfig;
import com.rtdi.rtdiplatform.core.exception.ResourceNotFoundErrorMessages;
import com.rtdi.rtdiplatform.core.exception.ResourceNotFoundException;
import com.rtdi.rtdiplatform.feature.delay.model.entity.DeliveryException;
import com.rtdi.rtdiplatform.feature.delay.model.entity.DeliveryExceptionType;
import com.rtdi.rtdiplatform.feature.delay.repository.DeliveryExceptionRepository;
import com.rtdi.rtdiplatform.feature.event.model.dto.ShipmentEventResponse;
import com.rtdi.rtdiplatform.feature.shipment.model.entity.Shipment;
import com.rtdi.rtdiplatform.feature.shipment.repository.ShipmentRepository;
import org.jspecify.annotations.NonNull;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DelayDetectionConsumerService {
    private final ShipmentRepository shipmentRepository;
    private final DeliveryExceptionRepository deliveryExceptionRepository;

    public DelayDetectionConsumerService(
            ShipmentRepository shipmentRepository,
            DeliveryExceptionRepository deliveryExceptionRepository) {
        this.shipmentRepository = shipmentRepository;
        this.deliveryExceptionRepository = deliveryExceptionRepository;
    }

    @Transactional
    @KafkaListener(topics = KafkaTopicConfig.SHIPMENT_EVENTS_TOPIC, groupId = "delay-detector-group")
    public void detectShipmentDelay(ShipmentEventResponse shipmentEventResponse) {
        Shipment shipment = shipmentRepository.findById(shipmentEventResponse.shipmentId())
                .orElseThrow(() -> new ResourceNotFoundException(ResourceNotFoundErrorMessages.SHIPMENT_NOT_FOUND));

        // check if EDA is smaller than current date
        if (shipmentEventResponse.eventTimestamp().isAfter(shipment.getExpectedDeliveryAt())) {
            DeliveryException deliveryException = createDeliveryException(shipmentEventResponse, shipment);
            deliveryExceptionRepository.save(deliveryException);
        }
    }

    private static @NonNull DeliveryException createDeliveryException(ShipmentEventResponse shipmentEventResponse, Shipment shipment) {
        DeliveryException deliveryException = new DeliveryException();
        deliveryException.setExceptionType(DeliveryExceptionType.SLA_BREACH_DELAY);
        deliveryException.setReason("Event received at " + shipmentEventResponse.eventTimestamp() + " which exceeds expected delivery SLA of " + shipment.getExpectedDeliveryAt());
        deliveryException.setShipmentId(shipmentEventResponse.shipmentId());
        deliveryException.setNotified(false);
        deliveryException.setResolved(false);
        return deliveryException;
    }
}
