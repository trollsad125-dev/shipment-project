package com.application.shipment.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(
        name = "shipment_items",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_shipment_item_line",
                columnNames = {"shipment_id", "line_number"}
        )
)
@Getter
@Setter
@NoArgsConstructor
@Entity
public class ShipmentItemJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "shipment_id", nullable = false)
    private ShipmentJpaEntity shipment;

    @Column(name = "line_number", nullable = false)
    private int lineNumber;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private int quantity;
}
