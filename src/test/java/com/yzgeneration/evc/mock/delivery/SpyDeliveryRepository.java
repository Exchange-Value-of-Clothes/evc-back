package com.yzgeneration.evc.mock.delivery;

import com.yzgeneration.evc.common.dto.SliceResponse;
import com.yzgeneration.evc.domain.delivery.dto.MobilityCreate;
import com.yzgeneration.evc.domain.delivery.infrastructure.DeliveryRepository;
import com.yzgeneration.evc.domain.delivery.model.Delivery;
import com.yzgeneration.evc.domain.delivery.model.DeliveryView;
import com.yzgeneration.evc.domain.item.enums.ItemType;
import com.yzgeneration.evc.exception.CustomException;
import com.yzgeneration.evc.exception.ErrorCode;
import com.yzgeneration.evc.fixture.DeliveryFixture;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SpyDeliveryRepository implements DeliveryRepository {

    private final List<Delivery> data = new ArrayList<>();

    @Override
    public Delivery save(Delivery delivery) {
        if (delivery.getOrderId() == null) {
            Delivery newDelivery = Delivery.builder()
                    .orderId(UUID.randomUUID().toString())
                    .senderId(delivery.getSenderId())
                    .receiverId(delivery.getReceiverId())
                    .deliveryStatus(delivery.getDeliveryStatus())
                    .itemType(delivery.getItemType())
                    .itemId(delivery.getItemId())
                    .build();
            data.add(newDelivery);
            return newDelivery;
        } else {
            data.removeIf(item -> item.getOrderId().equals(delivery.getOrderId()));
            data.add(delivery);
            return delivery;
        }
    }

    @Override
    public MobilityCreate createInfo(String orderId, ItemType itemType, Long itemId) {
        return DeliveryFixture.createDeliveryCreate(get(orderId));
    }

    @Override
    public Delivery get(String orderId) {
        for (Delivery datum : data) {
            if (datum.getOrderId().equals(orderId)) {
                return datum;
            }
        }
        throw new CustomException(ErrorCode.DELIVERY_NOT_FOUND);
    }

    @Override
    public SliceResponse<DeliveryView> findList(Long memberId, LocalDateTime cursor) {
        return null;
    }
}
