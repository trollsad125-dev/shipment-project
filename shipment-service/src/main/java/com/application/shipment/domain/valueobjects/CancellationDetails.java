package com.application.shipment.domain.valueobjects;

import com.application.shipment.domain.exception.InvalidShipmentDataException;
import io.micrometer.common.util.StringUtils;

import java.time.Instant;

public record CancellationDetails(String reason,
                                  Instant cancelledAt) {

    public CancellationDetails {
        if(StringUtils.isEmpty(reason)){
            throw new InvalidShipmentDataException("Reason is required");
        }

        if(cancelledAt == null){
            throw new InvalidShipmentDataException("Cancelled Time is required");
        }

        reason = reason.trim();
    }
}
