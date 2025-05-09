package com.yzgeneration.evc.domain.delivery.infrastructure;

import com.yzgeneration.evc.common.dto.SliceResponse;
import com.yzgeneration.evc.domain.delivery.dto.MobilityCreate;
import com.yzgeneration.evc.domain.delivery.model.Delivery;
import com.yzgeneration.evc.domain.delivery.model.DeliveryView;
import com.yzgeneration.evc.domain.image.enums.ItemType;

import java.time.LocalDateTime;


public interface DeliveryRepository {
    Delivery save(Delivery delivery);
    MobilityCreate createInfo(String orderId, ItemType itemType, Long itemId);
    Delivery get(String orderId);
    SliceResponse<DeliveryView> findList(Long memberId, LocalDateTime cursor);
}
