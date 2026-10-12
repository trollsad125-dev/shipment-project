package com.application.shipment.interfaces.rest.exception;

import java.time.Instant;
import java.util.List;

public record ApiErrorResponse(Instant timestamp,
                               int statusCode,
                               String code,
                               String message,
                               String path,
                               String traceId,
                               List<ErrorResponse> errors){

    public record ErrorResponse(String field,
                                String code,
                                String message){}

}
