package com.application.shipment.domain.exception;

public class InvalidShipmentStateException extends RuntimeException{

    public InvalidShipmentStateException(String message){
        super(message);
    }
}
