package com.application.shipment.domain.valueobjects;

import com.application.shipment.domain.exception.InvalidShipmentDataException;
import io.micrometer.common.util.StringUtils;

public record Contact(String name,
                      String phone) {

    public Contact{
        if(StringUtils.isEmpty(name)){
            throw new InvalidShipmentDataException("Name is required");
        }

        if(StringUtils.isEmpty(phone)){
            throw new InvalidShipmentDataException("Name is required");
        }

        name = name.trim();
        phone = phone.trim();
        String pattern = "\\d{10}|(?:\\d{3}-){2}\\d{4}|\\(\\d{3}\\)\\d{3}-?\\d{4}";

        if(!phone.matches(pattern)){
            throw new InvalidShipmentDataException("Phone only support Vietnam mobile number!");
        }
    }
}
