package com.application.shipment.interfaces.rest.request;

import java.math.BigDecimal;
import java.util.List;

public record CreateShipmentRequest(AddressRequest origin,
                                    AddressRequest destination,
                                    ContactRequest sender,
                                    ContactRequest recipient,
                                    BigDecimal weightKg,
                                    String serviceType,
                                    List<ShipmentItemRequest> items
                                    ) {

    public record AddressRequest(String addressLine,
                                 String city,
                                 String countryCode){}

    public record ContactRequest(String name,
                                 String phone){}

    public record ShipmentItemRequest(String description,
                                     int quantity){}
}
