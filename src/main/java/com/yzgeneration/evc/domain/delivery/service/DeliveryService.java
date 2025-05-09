package com.yzgeneration.evc.domain.delivery.service;

import com.yzgeneration.evc.common.dto.SliceResponse;
import com.yzgeneration.evc.domain.delivery.dto.MobilityCreate;
import com.yzgeneration.evc.domain.delivery.impl.DeliveryProcessor;
import com.yzgeneration.evc.domain.delivery.model.Delivery;
import com.yzgeneration.evc.domain.delivery.model.DeliveryView;
import com.yzgeneration.evc.domain.image.enums.ItemType;
import com.yzgeneration.evc.domain.image.infrastructure.repository.ProfileImageRepository;
import com.yzgeneration.evc.exception.CustomException;
import com.yzgeneration.evc.exception.ErrorCode;
import com.yzgeneration.evc.external.delivery.GetKakaoMobilityOrder;
import com.yzgeneration.evc.external.delivery.KakaoMobilityOrderResponse;
import com.yzgeneration.evc.external.delivery.Mobility;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DeliveryService {

    private final DeliveryProcessor deliveryProcessor;
    private final Mobility mobility;
    private final ProfileImageRepository profileImageRepository;

    public KakaoMobilityOrderResponse order(ItemType itemType, Long itemId, Long buyerId, Long sellerId, Long memberId) {
        if (!sellerId.equals(memberId)) throw new CustomException(ErrorCode.DELIVERY_ONLY_SELLER_REQUEST);
        Delivery delivery = deliveryProcessor.create(itemType, itemId, buyerId, sellerId);
        MobilityCreate mobilityCreate = deliveryProcessor.prepareForMobility(delivery.getOrderId(), itemType, itemId);
        KakaoMobilityOrderResponse response = mobility.delivery(mobilityCreate);
        storeForInquiry(buyerId, sellerId, memberId, delivery, mobilityCreate);
        return response;
    }

    public GetKakaoMobilityOrder get(String orderId, Long memberId) {
        deliveryProcessor.validRelatedness(orderId, memberId);
        return mobility.get(orderId);
    }

    private void storeForInquiry(Long buyerId, Long sellerId, Long memberId, Delivery delivery, MobilityCreate mobilityCreate) {
        Long otherPersonId = memberId.equals(buyerId) ? sellerId : memberId;
        String profileImageUrl = profileImageRepository.findById(otherPersonId).get().getImageUrl();
        deliveryProcessor.order(delivery, mobilityCreate.getItemName(), profileImageUrl, otherPersonId);
    }

    public SliceResponse<DeliveryView> findList(LocalDateTime cursor, Long memberId) {
        return deliveryProcessor.findList(memberId, cursor);
    }
}
