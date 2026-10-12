package com.application.shipment.domain.model;

import com.application.shipment.domain.exception.InvalidShipmentDataException;
import io.micrometer.common.util.StringUtils;

public record ShipmentItem(int lineNumber,
                           String description,
                           int quantity) {

    public ShipmentItem{
        if(lineNumber <= 0){
            throw new InvalidShipmentDataException("Line Number must be greater than ZERO");
        }

        if(StringUtils.isEmpty(description)){
            throw new InvalidShipmentDataException("Item description is required.");
        }

        if(quantity <= 0){
            throw  new InvalidShipmentDataException("Quantity must be greater than ZERO");
        }
        description = description.trim();

    }
}
