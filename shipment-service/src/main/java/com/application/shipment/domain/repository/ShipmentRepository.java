package com.application.shipment.domain.repository;

import com.application.shipment.domain.model.Shipment;

import java.util.Optional;
import java.util.UUID;

public interface ShipmentRepository {

    Optional<Shipment> findById(UUID shipmentId);

    Shipment save(Shipment shipment);

    Shipment updateShipmentStatus(Shipment shipment);

}
