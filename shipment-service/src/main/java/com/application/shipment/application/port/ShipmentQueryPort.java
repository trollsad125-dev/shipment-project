package com.application.shipment.application.port;

import com.application.shipment.application.query.GetShipmentListQuery;
import com.application.shipment.application.result.ShipmentListResult;

public interface ShipmentQueryPort {
    ShipmentListResult findAll(GetShipmentListQuery query);

}
