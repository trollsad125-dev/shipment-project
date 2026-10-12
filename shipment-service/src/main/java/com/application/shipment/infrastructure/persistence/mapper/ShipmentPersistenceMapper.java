package com.application.shipment.infrastructure.persistence.mapper;

import com.application.shipment.domain.model.Shipment;
import com.application.shipment.domain.model.ShipmentItem;
import com.application.shipment.domain.model.ShipmentStatus;
import com.application.shipment.domain.valueobjects.*;
import com.application.shipment.infrastructure.persistence.entity.ShipmentItemJpaEntity;
import com.application.shipment.infrastructure.persistence.entity.ShipmentJpaEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ShipmentPersistenceMapper {

    public ShipmentJpaEntity toEntity (Shipment shipment){
        ShipmentJpaEntity entity = new ShipmentJpaEntity();

        entity.setId(shipment.getId());
        entity.setCustomerId(shipment.getCustomerId());
        entity.setStatus(shipment.getStatus().name());
        entity.setServiceType(shipment.getServiceType().name());
        entity.setWeight(shipment.getWeight().kilograms());
        entity.setOriginAddressLine(shipment.getOrigin().addressLine());
        entity.setOriginCity(shipment.getOrigin().city());
        entity.setOriginCountryCode(shipment.getOrigin().countryCode());

        entity.setDestinationAddressLine(shipment.getDestination().addressLine());
        entity.setDestinationCity(shipment.getDestination().city());
        entity.setDestinationCountryCode(shipment.getDestination().countryCode());

        entity.setSenderName(shipment.getSender().name());
        entity.setSenderPhone(shipment.getSender().phone());

        entity.setRecipientName(shipment.getRecipient().name());
        entity.setRecipientPhone(shipment.getRecipient().phone());

        entity.setCreatedAt(shipment.getCreatedAt());
        entity.setUpdatedAt(shipment.getUpdatedAt());
        if (shipment.getCancellationDetails() != null) {
            entity.setCancellationReason(shipment.getCancellationDetails().reason());
            entity.setCancelledAt(shipment.getCancellationDetails().cancelledAt());
        }


        for(ShipmentItem item: shipment.getItems()){
            ShipmentItemJpaEntity itemEntity =
                    new ShipmentItemJpaEntity();

            itemEntity.setLineNumber(item.lineNumber());
            itemEntity.setDescription(item.description());
            itemEntity.setQuantity(item.quantity());

            entity.addItem(itemEntity);

        }
        return entity;
    }



    public Shipment toDomain(ShipmentJpaEntity entity){
        Address originAddress = new Address(entity.getOriginAddressLine(),
                entity.getOriginCity(),
                entity.getOriginCountryCode());
        Address destinationAddress = new Address(entity.getDestinationAddressLine(),
                entity.getDestinationCity(),
                entity.getDestinationCountryCode());

        Contact sender = new Contact(entity.getSenderName(),entity.getSenderPhone());
        Contact recipient = new Contact(entity.getRecipientName(), entity.getRecipientPhone());
        ConfirmationDetails confirmationDetails = null;

        if (entity.getBookingReference() != null
                || entity.getCaptureReference() != null
                || entity.getConfirmedAt() != null) {

            confirmationDetails = new ConfirmationDetails(
                    entity.getBookingReference(),
                    entity.getCaptureReference(),
                    entity.getConfirmedAt()
            );
        }

        CancellationDetails cancellationDtls = null;

        if (entity.getCancellationReason() != null
                || entity.getCancelledAt() != null) {

            cancellationDtls = new CancellationDetails(
                    entity.getCancellationReason(),
                    entity.getCancelledAt()
            );
        }
        List<ShipmentItem> itemList = new ArrayList<>();

        for(ShipmentItemJpaEntity entityItem : entity.getItems()){
            ShipmentItem item = new ShipmentItem(entityItem.getLineNumber(),entityItem.getDescription(), entityItem.getQuantity());
            itemList.add(item);
        }


        //Keep confirmation is null because currently no implementation
        return  Shipment.restore(entity.getId(),
                entity.getCustomerId(),
                originAddress,
                destinationAddress,
                sender,
                recipient,
                new Weight(entity.getWeight()),
                ServiceType.valueOf(entity.getServiceType()),
                itemList,
                ShipmentStatus.valueOf(entity.getStatus()),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                confirmationDetails,
                cancellationDtls);

    }

    public void updateCancellation(
            Shipment shipment,
            ShipmentJpaEntity entity) {

        CancellationDetails cancellation =
                shipment.getCancellationDetails();

        entity.setStatus(shipment.getStatus().name());
        entity.setUpdatedAt(shipment.getUpdatedAt());
        entity.setCancellationReason(cancellation.reason());
        entity.setCancelledAt(cancellation.cancelledAt());
    }
}
