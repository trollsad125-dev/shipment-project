package com.application.shipment.domain.model;

import com.application.shipment.domain.exception.InvalidShipmentDataException;
import com.application.shipment.domain.exception.InvalidShipmentStateException;
import com.application.shipment.domain.valueobjects.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Getter
@AllArgsConstructor
public final class Shipment {


    private final UUID id;
    private final UUID customerId;

    private final Address origin;
    private final Address destination;

    private final Contact sender;
    private final Contact recipient;

    private final Weight weight;
    private final ServiceType serviceType;
    private final List<ShipmentItem> items;

    private final Instant createdAt;

    private ShipmentStatus status;
    private Instant updatedAt;

    private ConfirmationDetails confirmationDetails;
    private CancellationDetails cancellationDetails;

    private Shipment(UUID id, UUID customerId, Address origin, Address destination, Contact sender, Contact recipient, Weight weight, ServiceType serviceType, List<ShipmentItem> items, Instant createdAt) {
        if (id == null || customerId == null) {
            throw new InvalidShipmentDataException(
                    "Shipment ID and customer ID are required"
            );
        }

        if (origin == null || destination == null) {
            throw new InvalidShipmentDataException(
                    "Origin and destination are required"
            );
        }

        if (sender == null || recipient == null) {
            throw new InvalidShipmentDataException(
                    "Sender and recipient are required"
            );
        }

        if (weight == null || serviceType == null) {
            throw new InvalidShipmentDataException(
                    "Weight and service type are required"
            );
        }

        if (createdAt == null) {
            throw new InvalidShipmentDataException(
                    "Creation time is required"
            );
        }
        validateItems(items);

        this.id = id;
        this.customerId = customerId;
        this.origin = origin;
        this.destination = destination;
        this.sender = sender;
        this.recipient = recipient;
        this.weight = weight;
        this.serviceType = serviceType;
        this.items = List.copyOf(items);
        this.status= ShipmentStatus.PENDING;
        this.createdAt = createdAt;
        this.updatedAt = createdAt;
    }



    public static Shipment create(
            UUID id,
            UUID customerId,
            Address origin,
            Address destination,
            Contact sender,
            Contact recipient,
            Weight weight,
            ServiceType serviceType,
            List<ShipmentItem> items,
            Instant now
    ) {
        return new Shipment(
                id,
                customerId,
                origin,
                destination,
                sender,
                recipient,
                weight,
                serviceType,
                items,
                now
        );
    }

    public static Shipment restore(
            UUID id,
            UUID customerId,
            Address origin,
            Address destination,
            Contact sender,
            Contact recipient,
            Weight weight,
            ServiceType serviceType,
            List<ShipmentItem> items,
            ShipmentStatus status,
            Instant createdAt,
            Instant updatedAt,
            ConfirmationDetails confirmation,
            CancellationDetails cancellation
    ){
        return new Shipment(id,
                customerId,
                origin,
                destination,
                sender,
                recipient,
                weight,
                serviceType,
                items,
                createdAt,
                status,
                updatedAt,
                confirmation,
                cancellation);
    }

    public void  confirm(String bookingReference,
                         String captureReferences,
                         Instant now){
        ConfirmationDetails requested = new ConfirmationDetails(bookingReference,captureReferences,now);

        if(status == ShipmentStatus.CONFIRMED){
            boolean sameReferences = confirmationDetails.bookingReference().equals(requested.bookingReference())
                                    && confirmationDetails.captureReference().equals(requested.captureReference());

            if(sameReferences){
                return;
            }
            throw new InvalidShipmentStateException("Shipment was already CONFIRMED with difference reference");
        }
        requirePending("confirmed");
        validateActionTime(now);

        this.confirmationDetails = requested;
        this.status = ShipmentStatus.CONFIRMED;
        this.updatedAt = now;
    }

    public void cancel(String reason, Instant now){
        CancellationDetails requested = new CancellationDetails(reason,now);

        if(status == ShipmentStatus.CANCELLED){
            if(cancellationDetails.reason().equals(requested.reason())){
                return;
            }
            throw new InvalidShipmentStateException("Shipment was already CANCELLED with different reason");
        }

        requirePending("cancelled");
        validateActionTime(now);

        this.cancellationDetails = requested;
        this.status = ShipmentStatus.CANCELLED;
        this.updatedAt = now;
    }


    private static void validateItems(List<ShipmentItem> items) {
        if (items == null || items.isEmpty()) {
            throw new InvalidShipmentDataException(
                    "Shipment must contain at least one item"
            );
        }

        Set<Integer> lineNumbers = new HashSet<>();

        for (ShipmentItem item : items) {
            if (item == null) {
                throw new InvalidShipmentDataException(
                        "Shipment item cannot be null"
                );
            }

            if (!lineNumbers.add(item.lineNumber())) {
                throw new InvalidShipmentDataException(
                        "Duplicate item line number: " + item.lineNumber()
                );
            }
        }
    }

    private void requirePending(String action) {
        if (status != ShipmentStatus.PENDING) {
            throw new InvalidShipmentStateException(
                    "Cannot " + action + " shipment in status " + status
            );
        }
    }

    private void validateActionTime(Instant now){
        if(now.isBefore(updatedAt)){
            throw new InvalidShipmentDataException("Action time cannot be earlier than latest change");
        }
    }

}
