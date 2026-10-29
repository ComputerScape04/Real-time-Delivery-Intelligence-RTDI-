package com.rtdi.rtdiplatform.feature.shipment.repository;

import com.rtdi.rtdiplatform.feature.shipment.model.entity.Shipment;
import com.rtdi.rtdiplatform.feature.shipment.model.entity.ShipmentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ShipmentRepository extends JpaRepository<Shipment, UUID> {

    Optional<Shipment> findByTrackingNumber(String trackingNumber);

    Page<Shipment> findByStatus(ShipmentStatus status, Pageable pageable);

    // checking for duplicates and discarding them
    boolean existsByTrackingNumber(String trackingNumber);

    // delayed shipments
    List<Shipment> findByStatusNotInAndExpectedDeliveryAtBefore(Collection<ShipmentStatus> shipmentStatuses, Instant cutoff);
}
