package com.application.shipment.application.query;

import com.application.shipment.application.exception.ShipmentNotFoundException;
import com.application.shipment.application.port.ShipmentQueryPort;
import com.application.shipment.application.result.ShipmentListResult;
import com.application.shipment.application.result.ShipmentResult;
import com.application.shipment.domain.model.Shipment;
import com.application.shipment.domain.repository.ShipmentRepository;
import org.springframework.stereotype.Service;

@Service
public class ShipmentQueryService {

    private final ShipmentRepository shipmentRepository;
    private final ShipmentQueryPort shipmentQueryPort;

    public ShipmentQueryService(ShipmentRepository shipmentRepository, ShipmentQueryPort shipmentQueryPort) {
        this.shipmentRepository = shipmentRepository;
        this.shipmentQueryPort = shipmentQueryPort;
    }


    public ShipmentResult getById(GetShipmentQuery shipmentQuery) {
        Shipment shipment = shipmentRepository.findById(shipmentQuery.shipmentId())
                .orElseThrow(() -> new ShipmentNotFoundException(shipmentQuery.shipmentId()));

        return ShipmentResult.from(shipment);
    }

    public ShipmentListResult getShipmentList(GetShipmentListQuery shipmentQuery) {

        return shipmentQueryPort.findAll(shipmentQuery);
    }
}
