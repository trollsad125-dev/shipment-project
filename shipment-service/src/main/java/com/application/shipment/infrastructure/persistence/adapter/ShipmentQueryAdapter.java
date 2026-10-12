package com.application.shipment.infrastructure.persistence.adapter;

import com.application.shipment.application.port.ShipmentQueryPort;
import com.application.shipment.application.query.GetShipmentListQuery;
import com.application.shipment.application.result.ShipmentListResult;
import com.application.shipment.application.result.ShipmentResult;
import com.application.shipment.infrastructure.persistence.entity.ShipmentJpaEntity;
import com.application.shipment.infrastructure.persistence.mapper.ShipmentPersistenceMapper;
import com.application.shipment.infrastructure.persistence.repository.ShipmentJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ShipmentQueryAdapter implements ShipmentQueryPort {

    private final ShipmentPersistenceMapper mapper;
    private final ShipmentJpaRepository repository;

    public ShipmentQueryAdapter(ShipmentPersistenceMapper mapper, ShipmentJpaRepository repository) {
        this.mapper = mapper;
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public ShipmentListResult findAll(GetShipmentListQuery query) {
        Pageable pageable = PageRequest.of(
          query.page(),
          query.size(),
                Sort.by(
                        Sort.Order.desc("createdAt"),
                        Sort.Order.desc("id")
                )
        );
        Page<ShipmentJpaEntity> entityPage = repository.findAll(pageable);

        List<ShipmentResult> shipmentResultList = new ArrayList<>();
        for(ShipmentJpaEntity entity: entityPage.getContent()){
            ShipmentResult result = ShipmentResult.from(mapper.toDomain(entity));
            shipmentResultList.add(result);
        }
        return new ShipmentListResult(entityPage.getNumber(), entityPage.getSize(), entityPage.getTotalPages(), entityPage.getTotalElements(), shipmentResultList);
    }
}
