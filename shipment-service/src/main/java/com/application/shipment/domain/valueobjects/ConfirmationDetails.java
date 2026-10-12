package com.application.shipment.domain.valueobjects;

import com.application.shipment.domain.exception.InvalidShipmentDataException;
import io.micrometer.common.util.StringUtils;

import java.time.Instant;

public record ConfirmationDetails(String bookingReference,
                                  String captureReference,
                                  Instant confirmedAt) {

    public ConfirmationDetails{
        if(StringUtils.isEmpty(bookingReference)){
            throw new InvalidShipmentDataException("Booking Reference is required");
        }

        if(StringUtils.isEmpty(captureReference)){
            throw new InvalidShipmentDataException("Capture Reference is required");
        }

        if(confirmedAt == null){
            throw new InvalidShipmentDataException("Confirmed Time is required");
        }

        bookingReference = bookingReference.trim();
        captureReference = captureReference.trim();
    }
}
