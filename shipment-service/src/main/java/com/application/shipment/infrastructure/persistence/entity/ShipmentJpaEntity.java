package com.application.shipment.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "shipments")
@Getter
@Setter
@NoArgsConstructor
public class ShipmentJpaEntity {

    @Id
    private UUID id;

    @Version
    private Long version;

    @Column(name = "customer_id",nullable = false)
    private UUID customerId;

    @Column(name = "status",nullable = false)
    private String status;

    @Column(name = "service_type",nullable = false)
    private String serviceType;

    @Column(name = "weight_kg",nullable = false,columnDefinition = "numeric")
    private BigDecimal weight;

    @Column(name = "origin_address_line",nullable = false)
    private String originAddressLine;

    @Column(name = "origin_city",nullable = false)
    private String originCity;

    @Column(name = "origin_country_code",nullable = false)
    private String originCountryCode;

    @Column(name = "destination_address_line",nullable = false)
    private String destinationAddressLine;

    @Column(name = "destination_city",nullable = false)
    private String destinationCity;

    @Column(name = "destination_country_code",nullable = false)
    private String destinationCountryCode;

    @Column(name = "sender_name",nullable = false)
    private String senderName;

    @Column(name = "sender_phone",nullable = false)
    private String senderPhone;

    @Column(name = "recipient_name",nullable = false)
    private String recipientName;

    @Column(name = "recipient_phone",nullable = false)
    private String recipientPhone;

    @Column(name = "created_at",nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at",nullable = false)
    private Instant updatedAt;

    @Column(name = "cancellation_reason", columnDefinition = "text")
    private String cancellationReason;

    @Column(name = "cancelled_at")
    private Instant cancelledAt;

    @Column(name = "confirmed_at")
    private Instant confirmedAt;

    @Column(name ="booking_reference")
    private String bookingReference;

    @Column(name ="capture_reference")
    private String captureReference;

    @OneToMany(mappedBy = "shipment",cascade = CascadeType.PERSIST)
    private List<ShipmentItemJpaEntity> items = new ArrayList<>();

    public void addItem(ShipmentItemJpaEntity item){
        items.add(item);
        item.setShipment(this);
    }


}
