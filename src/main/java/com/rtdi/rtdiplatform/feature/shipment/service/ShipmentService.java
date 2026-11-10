package com.rtdi.rtdiplatform.feature.shipment.service;

import com.rtdi.rtdiplatform.core.exception.ResourceNotFoundErrorMessages;
import com.rtdi.rtdiplatform.core.exception.ResourceNotFoundException;
import com.rtdi.rtdiplatform.feature.shipment.model.dto.CreateShipmentRequest;
import com.rtdi.rtdiplatform.feature.shipment.model.dto.ShipmentResponse;
import com.rtdi.rtdiplatform.feature.shipment.model.dto.UpdateShipmentRequest;
import com.rtdi.rtdiplatform.feature.shipment.model.entity.Shipment;
import com.rtdi.rtdiplatform.feature.shipment.model.entity.ShipmentStatus;
import com.rtdi.rtdiplatform.feature.shipment.repository.ShipmentRepository;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class ShipmentService {

    private final ShipmentRepository shipmentRepository;

    public ShipmentService(ShipmentRepository shipmentRepository) {
        this.shipmentRepository = shipmentRepository;
    }

    @Transactional
    public ShipmentResponse createShipment(CreateShipmentRequest createShipmentRequest) {
        // check if the same shipment exists already
        boolean shipmentExists = shipmentRepository.existsByTrackingNo(createShipmentRequest.trackingNo());
        if (shipmentExists) {
            throw new DuplicateKeyException("Shipment already exists");
        }

        Shipment shipment = new Shipment();

        // from the DTO
        shipment.setTrackingNo(createShipmentRequest.trackingNo());
        shipment.setOrigin(createShipmentRequest.origin());
        shipment.setDestination(createShipmentRequest.destination());
        shipment.setExpectedDeliveryAt(createShipmentRequest.expectedDeliveryAt());

        // set status to CREATED
        shipment.setStatus(ShipmentStatus.CREATED);

        // save it to DB
        Shipment saved = shipmentRepository.save(shipment);

        return ShipmentResponse.from(saved);
    }

    @Transactional
    public ShipmentResponse updateShipment(UpdateShipmentRequest updateShipmentRequest) {
        Shipment shipment = shipmentRepository.findByTrackingNo(updateShipmentRequest.trackingNo())
                .orElseThrow(() -> new ResourceNotFoundException(ResourceNotFoundErrorMessages.SHIPMENT_NOT_FOUND));

        shipment.setOrigin(updateShipmentRequest.origin());
        shipment.setDestination(updateShipmentRequest.destination());
        shipment.setExpectedDeliveryAt(updateShipmentRequest.expectedDeliveryAt());

        Shipment saved = shipmentRepository.save(shipment);
        return ShipmentResponse.from(saved);
    }

    @Transactional
    public void deleteShipment(String trackingNo) {

        Shipment shipment = shipmentRepository
                .findByTrackingNo(trackingNo)
                .orElseThrow(() -> new ResourceNotFoundException(ResourceNotFoundErrorMessages.SHIPMENT_NOT_FOUND));

        shipmentRepository.delete(shipment);
    }

    @Transactional(readOnly = true)
    public ShipmentResponse getShipmentByTrackingNo(String trackingNo) {
        Shipment shipment = shipmentRepository.findByTrackingNo(trackingNo)
                .orElseThrow(() -> new ResourceNotFoundException(ResourceNotFoundErrorMessages.SHIPMENT_NOT_FOUND));

        return ShipmentResponse.from(shipment);
    }
}
