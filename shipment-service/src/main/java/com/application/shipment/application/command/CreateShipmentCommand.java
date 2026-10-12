package com.application.shipment.application.command;

import com.application.shipment.domain.model.ShipmentItem;
import com.application.shipment.domain.valueobjects.Address;
import com.application.shipment.domain.valueobjects.Contact;
import com.application.shipment.domain.valueobjects.ServiceType;
import com.application.shipment.domain.valueobjects.Weight;

import java.time.Instant;
import java.util.List;

public record CreateShipmentCommand(Address origin,
                                    Address destination,
                                    Contact sender,
                                    Contact recipient,
                                    Weight weight,
                                    ServiceType serviceType,
                                    List<ShipmentItem> items,
                                    Instant now) {
}
