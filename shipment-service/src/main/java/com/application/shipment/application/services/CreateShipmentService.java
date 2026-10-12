package com.application.shipment.application.services;

import com.application.shipment.application.command.CreateShipmentCommand;
import com.application.shipment.application.result.ShipmentResult;
import com.application.shipment.domain.model.Shipment;
import com.application.shipment.domain.repository.ShipmentRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class CreateShipmentService {

    private final ShipmentRepository repository;

    public CreateShipmentService(ShipmentRepository repository) {
        this.repository = repository;
    }

    public ShipmentResult handle(CreateShipmentCommand command){
        Shipment shipment = Shipment.create(UUID.randomUUID(),
                UUID.randomUUID(),
                command.origin(),
                command.destination(),
                command.sender(),
                command.recipient(),
                command.weight(),
                command.serviceType(),
                command.items(),
                Instant.now());
        Shipment shipmentCreate = repository.save(shipment);
        return ShipmentResult.from(shipmentCreate);
    }
}
