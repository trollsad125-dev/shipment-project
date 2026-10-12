package com.application.shipment.interfaces.rest.response;

import lombok.Getter;

@Getter
public enum ShipmentSuccessMessage {
    CREATED("Shipment created successfully."),
    UPDATED("Shipment updated successfully."),
    CANCELLED("Shipment cancelled successfully.");

    private final String message;

    ShipmentSuccessMessage(String message) {
        this.message = message;
    }


}
