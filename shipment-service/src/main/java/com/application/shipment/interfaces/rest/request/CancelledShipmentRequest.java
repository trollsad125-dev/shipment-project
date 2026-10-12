package com.application.shipment.interfaces.rest.request;

import java.time.Instant;
import java.util.UUID;

public record CancelledShipmentRequest(UUID shipmentId, String reason) {

}
