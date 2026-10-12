package com.application.shipment.application.result;

import com.application.shipment.domain.model.Shipment;
import com.application.shipment.domain.model.ShipmentItem;
import com.application.shipment.domain.model.ShipmentStatus;
import com.application.shipment.domain.valueobjects.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ShipmentResult(
        UUID id,
        UUID customerId,
        ShipmentStatus status,

        Address origin,
        Address destination,

        Contact sender,
        Contact recipient,

        Weight weight,
        ServiceType serviceType,
        List<ShipmentItem> items,

        Instant createdAt,
        Instant updatedAt,

        ConfirmationDetails confirmation,
        CancellationDetails cancellation
    ) {
    public static ShipmentResult from (Shipment shipment){
        return new ShipmentResult(
                shipment.getId(),
                shipment.getCustomerId(),
                shipment.getStatus(),

                shipment.getOrigin(),
                shipment.getDestination(),

                shipment.getSender(),
                shipment.getRecipient(),

                shipment.getWeight(),
                shipment.getServiceType(),
                shipment.getItems(),

                shipment.getCreatedAt(),
                shipment.getUpdatedAt(),

                shipment.getConfirmationDetails(),
                shipment.getCancellationDetails()
                );
    }
}
