package com.rtdi.rtdiplatform.feature.delay.repository;

import com.rtdi.rtdiplatform.feature.delay.model.entity.DeliveryException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DeliveryExceptionRepository extends JpaRepository<DeliveryException, UUID> {

    // prevent alerts from being sent every minute if previously sent
    boolean existsByShipmentIdAndResolvedFalse(UUID shipmentId);

    List<DeliveryException> findByShipmentId(UUID shipmentId);

    List<DeliveryException> findByResolvedFalse();
}
