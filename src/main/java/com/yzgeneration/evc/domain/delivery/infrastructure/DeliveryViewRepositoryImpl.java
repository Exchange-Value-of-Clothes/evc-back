package com.yzgeneration.evc.domain.delivery.infrastructure;

import com.yzgeneration.evc.domain.delivery.model.DeliveryView;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class DeliveryViewRepositoryImpl implements DeliveryViewRepository {

    private final DeliveryViewJpaRepository deliveryViewJpaRepository;

    @Override
    public DeliveryView save(DeliveryView deliveryView) {
        return deliveryViewJpaRepository.save(DeliveryViewEntity.from(deliveryView)).toModel();
    }
}
