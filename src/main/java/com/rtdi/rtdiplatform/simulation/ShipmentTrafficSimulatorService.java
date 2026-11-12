package com.rtdi.rtdiplatform.simulation;

import com.rtdi.rtdiplatform.feature.event.model.dto.CreateShipmentEventRequest;
import com.rtdi.rtdiplatform.feature.event.model.entity.ShipmentEventType;
import com.rtdi.rtdiplatform.feature.event.service.ShipmentEventService;
import com.rtdi.rtdiplatform.feature.shipment.model.dto.CreateShipmentRequest;
import com.rtdi.rtdiplatform.feature.shipment.model.entity.Shipment;
import com.rtdi.rtdiplatform.feature.shipment.model.entity.ShipmentStatus;
import com.rtdi.rtdiplatform.feature.shipment.repository.ShipmentRepository;
import com.rtdi.rtdiplatform.feature.shipment.service.ShipmentService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class ShipmentTrafficSimulatorService {

    private final ShipmentService shipmentService;
    private final ShipmentEventService shipmentEventService;
    private final ShipmentRepository shipmentRepository;

    private static final List<String> CITIES = List.of(
            "New York, NY", "Los Angeles, CA", "Chicago, IL", "Houston, TX", "Phoenix, AZ", "Seattle, WA"
    );
    private static final List<String> CARRIERS = List.of("FedEx", "UPS", "DHL", "USPS");

    public ShipmentTrafficSimulatorService(
            ShipmentService shipmentService,
            ShipmentEventService shipmentEventService,
            ShipmentRepository shipmentRepository) {
        this.shipmentService = shipmentService;
        this.shipmentEventService = shipmentEventService;
        this.shipmentRepository = shipmentRepository;
    }

    @Scheduled(fixedRate = 3000) // every 3 seconds
    public void simulateTraffic() {
        // 30% chance of creating a new shipment or if none exist
        if (ThreadLocalRandom.current().nextInt(100) < 30) {
            spawnRandomShipment();
            return;
        }

        advanceActiveShipment();
    }

    public void spawnRandomShipment() {
        String trackingNo = "SIM-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        String origin = CITIES.get(ThreadLocalRandom.current().nextInt(CITIES.size()));
        String destination = CITIES.get(ThreadLocalRandom.current().nextInt(CITIES.size()));
        Instant expectedDeliveryDate = Instant.now().plus(ThreadLocalRandom.current().nextInt(2, 48), ChronoUnit.HOURS);
        shipmentService.createShipment(new CreateShipmentRequest(
                trackingNo, origin, destination, expectedDeliveryDate
        ));
    }

    public void advanceActiveShipment() {
        // fetch events that are created, in_transit, out_for_delivery
        List<Shipment> created = shipmentRepository.findByStatus(ShipmentStatus.CREATED, PageRequest.of(0, 10)).getContent();
        List<Shipment> inTransit = shipmentRepository.findByStatus(ShipmentStatus.IN_TRANSIT, PageRequest.of(0, 10)).getContent();
        List<Shipment> outForDelivery = shipmentRepository.findByStatus(ShipmentStatus.OUT_FOR_DELIVERY, PageRequest.of(0, 10)).getContent();

        // spawn a shipment instead
        if (created.isEmpty() && inTransit.isEmpty() && outForDelivery.isEmpty()) {
            spawnRandomShipment();
            return;
        }

        List<Shipment> activeShipments = new ArrayList<>();
        activeShipments.addAll(created);
        activeShipments.addAll(inTransit);
        activeShipments.addAll(outForDelivery);

        Shipment activeShipment = activeShipments.get(ThreadLocalRandom.current().nextInt(0, activeShipments.size()));

        ShipmentEventType nextEvent = switch (activeShipment.getStatus()) {
            case CREATED -> ShipmentEventType.PICKED_UP;
            case IN_TRANSIT -> ShipmentEventType.OUT_FOR_DELIVERY;
            case OUT_FOR_DELIVERY -> {
                yield (ThreadLocalRandom.current().nextInt(100) < 90)
                        ? ShipmentEventType.DELIVERED
                        : ShipmentEventType.CANCELLED;
            }
            default -> null;
        };

        if (nextEvent == null) {
            return;
        }

        CreateShipmentEventRequest shipmentEventRequest = new CreateShipmentEventRequest(
                UUID.randomUUID(),
                activeShipment.getId(),
                nextEvent,
                CARRIERS.get(ThreadLocalRandom.current().nextInt(0, CARRIERS.size())),
                activeShipment.getDestination(),
                Instant.now(),
                "{\"automated_simulation\": true}"
        );

        shipmentEventService.recordEvent(shipmentEventRequest);
    }

}
