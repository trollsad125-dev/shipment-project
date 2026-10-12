package com.application.shipment.interfaces.rest.response;

import java.util.List;

public record ShipmentListResponse(int page,
                                   int size,
                                   long totalCount,
                                   long totalElement,
                                   List<ShipmentResponse> itemsResponse
                                   ) {
}
