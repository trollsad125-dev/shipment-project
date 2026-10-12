package com.application.shipment.interfaces.rest;

import com.application.shipment.application.command.CreateShipmentCommand;
import com.application.shipment.application.command.UpdateShipmentCommand;
import com.application.shipment.application.query.GetShipmentListQuery;
import com.application.shipment.application.query.GetShipmentQuery;
import com.application.shipment.application.query.ShipmentQueryService;
import com.application.shipment.application.result.ShipmentListResult;
import com.application.shipment.application.result.ShipmentResult;
import com.application.shipment.application.services.UpdateShipmentService;
import com.application.shipment.application.services.CreateShipmentService;
import com.application.shipment.interfaces.rest.mapper.ShipmentInterfaceMapper;
import com.application.shipment.interfaces.rest.request.CancelledShipmentRequest;
import com.application.shipment.interfaces.rest.request.CreateShipmentRequest;
import com.application.shipment.interfaces.rest.request.ShipmentListRequest;
import com.application.shipment.interfaces.rest.response.ShipmentActionResponse;
import com.application.shipment.interfaces.rest.response.ShipmentListResponse;
import com.application.shipment.interfaces.rest.response.ShipmentResponse;
import com.application.shipment.interfaces.rest.response.ShipmentSuccessMessage;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.UUID;

@RestController
@RequestMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
public class ShipmentController {

    private final ShipmentInterfaceMapper interfaceMapper;
    private final CreateShipmentService createShipmentService;
    private final ShipmentQueryService shipmentQueryService;
    private final UpdateShipmentService updateShipmentService;

    private ShipmentController(ShipmentInterfaceMapper interfaceMapper, CreateShipmentService createShipmentService, ShipmentQueryService shipmentQueryService, UpdateShipmentService updateShipmentService){
        this.interfaceMapper = interfaceMapper;
        this.createShipmentService = createShipmentService;
        this.shipmentQueryService = shipmentQueryService;
        this.updateShipmentService = updateShipmentService;
    }

    @PostMapping(value = "shipment-create")
    public ResponseEntity<ShipmentActionResponse> create(@RequestBody CreateShipmentRequest shipmentRequest){
        CreateShipmentCommand createShipmentCommand = interfaceMapper.toCreateCommand(shipmentRequest);
        ShipmentResult result =
                createShipmentService.handle(createShipmentCommand);
        ShipmentActionResponse response = interfaceMapper.toActionResponse(result, ShipmentSuccessMessage.CREATED.getMessage());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping(value = "shipment/{shipment-id}")
    public ResponseEntity<ShipmentResponse> get(@PathVariable("shipment-id") UUID parameter){
        GetShipmentQuery query =  new GetShipmentQuery(parameter);
        ShipmentResult result = shipmentQueryService.getById(query);
        ShipmentResponse response = interfaceMapper.toResponse(result);

        return ResponseEntity.ok(response);
    }

    @GetMapping("shipments")
    public ResponseEntity<ShipmentListResponse> getListShipment(@Valid @ModelAttribute ShipmentListRequest request){
        GetShipmentListQuery query = new GetShipmentListQuery(request.page(),request.size());
        ShipmentListResult result = shipmentQueryService.getShipmentList(query);

        ShipmentListResponse responseList = interfaceMapper.toResponseList(result);
        return ResponseEntity.ok(responseList);
    }

    @PutMapping("shipment-cancelled")

    public ResponseEntity<ShipmentActionResponse> updateCancel(@RequestBody CancelledShipmentRequest cancelledShipmentRequest){
        UpdateShipmentCommand command = new UpdateShipmentCommand(cancelledShipmentRequest.shipmentId(),cancelledShipmentRequest.reason(), Instant.now());
        ShipmentResult result = updateShipmentService.updateCancelShipment(command);

        ShipmentActionResponse responseList = interfaceMapper.toActionResponse(result, ShipmentSuccessMessage.UPDATED.getMessage());
        return ResponseEntity.ok(responseList);
    }
}

