package com.application.shipment.domain.exception;

public class InvalidShipmentDataException extends RuntimeException{

    public InvalidShipmentDataException(String message){
        super(message);
    }
}
