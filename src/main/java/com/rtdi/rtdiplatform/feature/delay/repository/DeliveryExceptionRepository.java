package com.rtdi.rtdiplatform.feature.delay.repository;

import com.rtdi.rtdiplatform.feature.delay.model.entity.DeliveryException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DeliveryExceptionRepository extends JpaRepository<DeliveryException, UUID> {
}
