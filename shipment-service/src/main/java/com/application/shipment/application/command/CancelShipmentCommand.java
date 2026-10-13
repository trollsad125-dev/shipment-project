package com.application.shipment.application.command;

import java.time.Instant;
import java.util.UUID;

public record CancelShipmentCommand(UUID shipmentId, String reason, Instant updatedAt) {
}
