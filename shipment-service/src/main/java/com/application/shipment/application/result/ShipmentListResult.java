package com.application.shipment.application.result;

import com.application.shipment.domain.model.Shipment;

import java.util.List;

public record ShipmentListResult(
                                int page,
                                int size,
                                long totalCount,
                                long totalElements,
                                List<ShipmentResult> itemsResponse) {
}
