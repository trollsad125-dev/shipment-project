package com.application.shipment.application.services;

import com.application.shipment.application.command.CancelShipmentCommand;
import com.application.shipment.application.command.ConfirmShipmentCommand;
import com.application.shipment.application.exception.ShipmentNotFoundException;
import com.application.shipment.application.result.ShipmentResult;
import com.application.shipment.domain.exception.InvalidShipmentStateException;
import com.application.shipment.domain.model.Shipment;
import com.application.shipment.domain.model.ShipmentStatus;
import com.application.shipment.domain.repository.ShipmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;

@Service
public class UpdateShipmentService {

    private final ShipmentRepository repository;

    public UpdateShipmentService(ShipmentRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public ShipmentResult updateCancelShipment(CancelShipmentCommand command){
        Shipment shipment = repository.findById(command.shipmentId())
                .orElseThrow(() -> new ShipmentNotFoundException(command.shipmentId()));

        shipment.cancel(command.reason(), Instant.now());

        Shipment savedShipment = repository.updateShipmentStatus(shipment);

        return ShipmentResult.from(savedShipment);
    }

    @Transactional
    public ShipmentResult confirmShipment(ConfirmShipmentCommand command) {
        Shipment shipment = repository.findById(command.shipmentId())
                .orElseThrow(() -> new ShipmentNotFoundException(command.shipmentId()));
        // 2. Gọi shipment.confirm(...) với references và thời gian hiện tại
        shipment.confirm(command.bookingReference(),command.captureReference(),command.confirmedAt());
        // 3. Lưu confirmation vào entity hiện có
        Shipment savedShipment = repository.updateShipmentStatus(shipment);
        // 4. Trả ShipmentResult
        return ShipmentResult.from(savedShipment);
    }
}
