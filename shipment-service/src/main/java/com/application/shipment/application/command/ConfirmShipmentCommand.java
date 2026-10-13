package com.application.shipment.application.command;

import java.time.Instant;
import java.util.UUID;

public record ConfirmShipmentCommand(UUID shipmentId, String bookingReference, String captureReference,Instant confirmedAt) {
}
