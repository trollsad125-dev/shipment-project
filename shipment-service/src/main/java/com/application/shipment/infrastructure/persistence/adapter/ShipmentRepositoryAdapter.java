package com.application.shipment.infrastructure.persistence.adapter;

import com.application.shipment.application.exception.ShipmentNotFoundException;
import com.application.shipment.domain.exception.InvalidShipmentStateException;
import com.application.shipment.domain.model.Shipment;
import com.application.shipment.domain.model.ShipmentStatus;
import com.application.shipment.domain.repository.ShipmentRepository;
import com.application.shipment.infrastructure.persistence.entity.ShipmentJpaEntity;
import com.application.shipment.infrastructure.persistence.mapper.ShipmentPersistenceMapper;
import com.application.shipment.infrastructure.persistence.repository.ShipmentJpaRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class ShipmentRepositoryAdapter implements ShipmentRepository {

    private final ShipmentPersistenceMapper mapper;
    private final ShipmentJpaRepository repository;

    public ShipmentRepositoryAdapter(
            ShipmentJpaRepository repository,
            ShipmentPersistenceMapper mapper
    ) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Optional<Shipment> findById(UUID shipmentId) {
        Optional<ShipmentJpaEntity> entity = repository.findById(shipmentId);
        return entity.map(mapper::toDomain);

    }

    @Override
    @Transactional
    public Shipment save(Shipment shipment) {
        //Check status as PENDING
        if(shipment.getStatus() != ShipmentStatus.PENDING){
            throw new UnsupportedOperationException("This implementation currently supports creation only");

        }
        ShipmentJpaEntity entity = mapper.toEntity(shipment);

        return mapper.toDomain(repository.saveAndFlush(entity));

    }

    @Override
    @Transactional
    public Shipment updateShipmentStatus(Shipment shipment) {
        ShipmentJpaEntity entity = repository.findById(shipment.getId())
                .orElseThrow(
                        () -> new ShipmentNotFoundException(shipment.getId())
                );
        if (shipment.getStatus().equals(ShipmentStatus.CANCELLED)) {
            mapper.updateCancellation(shipment, entity);

        } else if (shipment.getStatus().equals(ShipmentStatus.CONFIRMED)) {
            mapper.updateConfirmed(shipment, entity);
        } else {
            throw new UnsupportedOperationException(
                    "Status update supports CANCELLED and CONFIRMED only."
            );
        }
            repository.flush();
        return mapper.toDomain(entity);


    }




}
