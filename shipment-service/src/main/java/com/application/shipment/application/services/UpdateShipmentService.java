package com.application.shipment.application.services;

import com.application.shipment.application.command.UpdateShipmentCommand;
import com.application.shipment.application.exception.ShipmentNotFoundException;
import com.application.shipment.application.result.ShipmentResult;
import com.application.shipment.domain.model.Shipment;
import com.application.shipment.domain.repository.ShipmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.util.UUID;

@Service
public class UpdateShipmentService {

    private final ShipmentRepository repository;

    public UpdateShipmentService(ShipmentRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public ShipmentResult updateCancelShipment(UpdateShipmentCommand command){
        Shipment shipment = repository.findById(command.shipmentId())
                .orElseThrow(() -> new ShipmentNotFoundException(command.shipmentId()));

        shipment.cancel(command.reason(), Instant.now());

        Shipment savedShipment = repository.updateShipmentStatus(shipment);

        return ShipmentResult.from(savedShipment);
    }
}
