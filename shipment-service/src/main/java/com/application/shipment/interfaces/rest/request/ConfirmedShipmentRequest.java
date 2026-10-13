package com.application.shipment.interfaces.rest.request;

import java.util.UUID;

public record ConfirmedShipmentRequest(UUID shipmentId, String bookingReference, String captureReference) {

}
