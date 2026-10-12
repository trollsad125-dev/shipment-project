package com.application.shipment.domain.valueobjects;

import com.application.shipment.domain.exception.InvalidShipmentDataException;
import io.micrometer.common.util.StringUtils;

import java.util.Locale;

public record Address(String addressLine,
                      String city,
                      String countryCode) {

    public Address {
        if(StringUtils.isEmpty(addressLine)){
            throw new InvalidShipmentDataException("Address Line is required");
        }

        if(StringUtils.isEmpty(city)){
            throw new InvalidShipmentDataException("City is required");
        }

        if(StringUtils.isEmpty(countryCode)){
            throw new InvalidShipmentDataException("Country Code is required");
        }

        addressLine = addressLine.trim();
        city = city.trim();
        countryCode = countryCode.trim().toUpperCase(Locale.ROOT);

        if (!countryCode.matches("[A-Z]{2}")) {
            throw new InvalidShipmentDataException(
                    "Country code must contain two letters"
            );
        }

    }

}
