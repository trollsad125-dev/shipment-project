package com.application.shipment.infrastructure.persistence.repository;

import com.application.shipment.infrastructure.persistence.entity.ShipmentJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ShipmentJpaRepository extends JpaRepository<ShipmentJpaEntity, UUID> {
}
