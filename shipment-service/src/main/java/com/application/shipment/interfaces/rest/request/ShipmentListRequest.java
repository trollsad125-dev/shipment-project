package com.application.shipment.interfaces.rest.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record ShipmentListRequest(
        @Min(value = 0, message = "page must be greater than or equal to 0")
        Integer page,
        @Min(value = 1, message = "size must be greater than or equal to 1")
        @Max(value = 100, message = "size must be less than or equal to 100")
        Integer size,
        String serviceType, String status){

    public ShipmentListRequest {
        page = page == null ? 0 : page;
        size = size == null ? 20 : size;
    }
}
