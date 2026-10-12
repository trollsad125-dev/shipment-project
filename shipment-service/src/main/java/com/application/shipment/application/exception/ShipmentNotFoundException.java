package com.application.shipment.application.exception;

import lombok.Getter;

import java.util.UUID;

@Getter
public class ShipmentNotFoundException extends RuntimeException{

    private final UUID shipmentId;

    public ShipmentNotFoundException(UUID shipmentId){
        super("Shipment not found: "+ shipmentId);
        this.shipmentId = shipmentId;
    }

}
