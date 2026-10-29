package com.rtdi.rtdiplatform.feature.event.repository;

import com.rtdi.rtdiplatform.feature.event.model.entity.ShipmentEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ShipmentEventRepository extends JpaRepository<ShipmentEvent, UUID> {

    // retrieving shipment events for a given shipment_id
    List<ShipmentEvent> findByShipmentIdOrderByEventTimestampAsc(UUID shipmentId);

    // check for duplicates and discard them
    boolean existsByEventId(UUID eventId);
}
