package com.application.shipment.interfaces.rest.response;

import java.util.UUID;

public record ShipmentActionResponse(UUID shipmentId,String status,String message) {


}
