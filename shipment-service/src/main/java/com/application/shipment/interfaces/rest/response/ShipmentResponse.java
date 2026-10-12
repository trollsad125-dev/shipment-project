package com.application.shipment.interfaces.rest.response;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ShipmentResponse(UUID id,
                               UUID customerId,
                               String status,

                               AddressResponse origin,
                               AddressResponse destination,

                               ContactResponse sender,
                               ContactResponse recipient,

                               BigDecimal weightKg,
                               String serviceType,
                               List<ShipmentItemResponse> itemResponse,
                               Instant createdAt,
                               Instant updatedAt,

                               ConfirmationResponse confirmationResponse,
                               CancellationResponse cancellationResponse
                               ) {

    public record AddressResponse(String addressLine,
                                  String city,
                                  String countryCode){}

    public record ContactResponse(String name,
                                  String phone){}

    public record ShipmentItemResponse(int lineNumber,
                                       String description,
                                       int quantity){}

    public record ConfirmationResponse(String bookingReference,
                                       String captureReference,
                                       Instant confirmedAt){}

    public record CancellationResponse(String reason,
                                       Instant cancelledAt){}
}
