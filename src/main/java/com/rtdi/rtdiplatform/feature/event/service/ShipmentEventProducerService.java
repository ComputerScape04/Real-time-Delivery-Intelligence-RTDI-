package com.rtdi.rtdiplatform.feature.event.service;

import com.rtdi.rtdiplatform.config.KafkaTopicConfig;
import com.rtdi.rtdiplatform.feature.event.model.dto.ShipmentEventResponse;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ShipmentEventProducerService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public ShipmentEventProducerService(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendShipmentEvent(String trackingNo, ShipmentEventResponse shipmentEventResponse) {
        kafkaTemplate.send(KafkaTopicConfig.SHIPMENT_EVENTS_TOPIC, trackingNo, shipmentEventResponse);
    }
}
