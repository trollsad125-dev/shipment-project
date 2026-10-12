package com.application.shipment.interfaces.rest.mapper;

import com.application.shipment.application.command.CreateShipmentCommand;
import com.application.shipment.application.result.ShipmentListResult;
import com.application.shipment.application.result.ShipmentResult;
import com.application.shipment.domain.model.ShipmentItem;
import com.application.shipment.domain.valueobjects.Address;
import com.application.shipment.domain.valueobjects.Contact;
import com.application.shipment.domain.valueobjects.ServiceType;
import com.application.shipment.domain.valueobjects.Weight;
import com.application.shipment.interfaces.rest.request.CreateShipmentRequest;
import com.application.shipment.interfaces.rest.response.ShipmentActionResponse;
import com.application.shipment.interfaces.rest.response.ShipmentListResponse;
import com.application.shipment.interfaces.rest.response.ShipmentResponse;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class ShipmentInterfaceMapper {

    public CreateShipmentCommand toCreateCommand(CreateShipmentRequest requested) {

        Address origin = new Address(requested.origin().addressLine(), requested.origin().city(), requested.origin().countryCode());
        Address destination = new Address(requested.destination().addressLine(), requested.destination().city(), requested.destination().countryCode());
        Contact sender = new Contact(requested.sender().name(), requested.sender().phone());
        Contact recipient = new Contact(requested.recipient().name(), requested.recipient().phone());
        Weight weight = new Weight(requested.weightKg());
        List<ShipmentItem> itemsList = new ArrayList<>();
        int lineNumber = 1;
        ServiceType serviceType = ServiceType.valueOf(
                requested.serviceType()
        );
        for (CreateShipmentRequest.ShipmentItemRequest itemsRequest : requested.items()) {
            itemsList.add(new ShipmentItem(lineNumber++, itemsRequest.description(), itemsRequest.quantity()));

        }

        return new CreateShipmentCommand(origin,
                destination,
                sender,
                recipient,
                weight,
                serviceType,
                itemsList,
                Instant.now()
        );
    }

    public ShipmentResponse toResponse(ShipmentResult result) {
        ShipmentResponse.AddressResponse origin =
                new ShipmentResponse.AddressResponse(
                        result.origin().addressLine(),
                        result.origin().city(),
                        result.origin().countryCode()
                );

        ShipmentResponse.AddressResponse destination =
                new ShipmentResponse.AddressResponse(
                        result.destination().addressLine(),
                        result.destination().city(),
                        result.destination().countryCode()
                );

        ShipmentResponse.ContactResponse sender =
                new ShipmentResponse.ContactResponse(
                        result.sender().name(),
                        result.sender().phone()
                );

        ShipmentResponse.ContactResponse recipient =
                new ShipmentResponse.ContactResponse(
                        result.recipient().name(),
                        result.recipient().phone()
                );

        List<ShipmentResponse.ShipmentItemResponse> items =
                new ArrayList<>();

        for (ShipmentItem item : result.items()) {
            items.add(new ShipmentResponse.ShipmentItemResponse(
                    item.lineNumber(),
                    item.description(),
                    item.quantity()
            ));
        }

        ShipmentResponse.ConfirmationResponse confirmation = null;

        if (result.confirmation() != null) {
            confirmation = new ShipmentResponse.ConfirmationResponse(
                    result.confirmation().bookingReference(),
                    result.confirmation().captureReference(),
                    result.confirmation().confirmedAt()
            );
        }

        ShipmentResponse.CancellationResponse cancellation = null;

        if (result.cancellation() != null) {
            cancellation = new ShipmentResponse.CancellationResponse(
                    result.cancellation().reason(),
                    result.cancellation().cancelledAt()
            );
        }

        return new ShipmentResponse(
                result.id(),
                result.customerId(),
                result.status().name(),

                origin,
                destination,

                sender,
                recipient,

                result.weight().kilograms(),
                result.serviceType().name(),
                List.copyOf(items),

                result.createdAt(),
                result.updatedAt(),

                confirmation,
                cancellation
        );
    }

    public ShipmentListResponse toResponseList(ShipmentListResult resultList) {
        List<ShipmentResponse> responses = new ArrayList<>();
        for(ShipmentResult result : resultList.itemsResponse()){
            ShipmentResponse response = toResponse(result);
            responses.add(response);
        }
        return new ShipmentListResponse(
                resultList.page(),
                resultList.size(),
                resultList.totalCount(),
                resultList.totalElements(),
                responses);

    }

    public ShipmentActionResponse toActionResponse(ShipmentResult result, String msg){
        return new ShipmentActionResponse(result.id(),result.status().name(),msg);
    }
}
