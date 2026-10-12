package com.application.shipment.domain.valueobjects;

import com.application.shipment.domain.exception.InvalidShipmentDataException;

import java.math.BigDecimal;

public record Weight(BigDecimal kilograms) {

    public Weight{

        if(kilograms == null){
            throw new InvalidShipmentDataException("Weight can't be null");
        }

        if(kilograms.compareTo(BigDecimal.ZERO) <= 0){
            throw new InvalidShipmentDataException("Weight can't be ZERO");
        }

        kilograms = kilograms.stripTrailingZeros();
    }
}
