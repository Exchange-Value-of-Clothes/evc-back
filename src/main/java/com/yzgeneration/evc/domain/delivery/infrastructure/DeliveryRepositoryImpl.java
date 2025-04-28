package com.yzgeneration.evc.domain.delivery.infrastructure;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yzgeneration.evc.domain.delivery.dto.DeliveryCreate;
import com.yzgeneration.evc.domain.delivery.model.Delivery;
import com.yzgeneration.evc.domain.image.enums.ItemType;

import com.yzgeneration.evc.domain.member.infrastructure.QMemberEntity;
import com.yzgeneration.evc.exception.CustomException;
import com.yzgeneration.evc.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.yzgeneration.evc.domain.delivery.infrastructure.QDeliveryEntity.*;
import static com.yzgeneration.evc.domain.item.auctionitem.infrastructure.entity.QAuctionItemEntity.auctionItemEntity;
import static com.yzgeneration.evc.domain.item.useditem.infrastructure.entity.QUsedItemEntity.usedItemEntity;

@Repository
@RequiredArgsConstructor
public class DeliveryRepositoryImpl implements DeliveryRepository {

    private final DeliveryJpaRepository jpaRepository;
    private final JPAQueryFactory queryFactory;

    @Override
    public Delivery save(Delivery delivery) {
        return jpaRepository.save(DeliveryEntity.from(delivery)).toModel();
    }

    @Override
    public DeliveryCreate createInfo(String orderId, ItemType itemType, Long itemId) {
        QMemberEntity sender = new QMemberEntity("sender");
        QMemberEntity receiver = new QMemberEntity("receiver");
        QAddressEntity senderAddress = new QAddressEntity("senderAddress");
        QAddressEntity receiverAddress = new QAddressEntity("receiverAddress");

        if (itemType == ItemType.USEDITEM) {
            return queryFactory.select(Projections.constructor(DeliveryCreate.class,
                            deliveryEntity.orderId,
                            usedItemEntity.itemDetailsEntity.price,
                            usedItemEntity.itemDetailsEntity.title,
                            sender.memberPrivateInformationEntity.accountName,
                            sender.memberPrivateInformationEntity.phoneNumber,
                            senderAddress.basicAddress,
                            senderAddress.detailAddress,
                            senderAddress.latitude,
                            senderAddress.longitude,
                            receiver.memberPrivateInformationEntity.accountName,
                            receiver.memberPrivateInformationEntity.phoneNumber,
                            receiverAddress.basicAddress,
                            receiverAddress.detailAddress,
                            receiverAddress.latitude,
                            receiverAddress.longitude))
                    .from(deliveryEntity)
                    .where(deliveryEntity.orderId.eq(orderId))
                    .leftJoin(sender).on(deliveryEntity.senderId.eq(sender.id))
                    .leftJoin(receiver).on(deliveryEntity.receiverId.eq(receiver.id))
                    .leftJoin(senderAddress).on(senderAddress.memberId.eq(sender.id))
                    .leftJoin(receiverAddress).on(receiverAddress.memberId.eq(receiver.id))
                    .leftJoin(usedItemEntity).on(usedItemEntity.id.eq(itemId))
                    .fetchFirst();
        } else {
            return queryFactory.select(Projections.constructor(DeliveryCreate.class,
                            deliveryEntity.orderId,
                            auctionItemEntity.auctionItemPriceDetailsEntity.currentPrice,
                            auctionItemEntity.auctionItemDetailsEntity.title,
                            sender.memberPrivateInformationEntity.accountName,
                            sender.memberPrivateInformationEntity.phoneNumber,
                            senderAddress.basicAddress,
                            senderAddress.detailAddress,
                            senderAddress.latitude,
                            senderAddress.longitude,
                            receiver.memberPrivateInformationEntity.accountName,
                            receiver.memberPrivateInformationEntity.phoneNumber,
                            receiverAddress.basicAddress,
                            receiverAddress.detailAddress,
                            receiverAddress.latitude,
                            receiverAddress.longitude))
                    .from(deliveryEntity)
                    .where(deliveryEntity.orderId.eq(orderId))
                    .leftJoin(sender).on(deliveryEntity.receiverId.eq(sender.id))
                    .leftJoin(receiver).on(deliveryEntity.senderId.eq(receiver.id))
                    .leftJoin(senderAddress).on(senderAddress.memberId.eq(sender.id))
                    .leftJoin(receiverAddress).on(receiverAddress.memberId.eq(receiver.id))
                    .leftJoin(auctionItemEntity).on(auctionItemEntity.id.eq(itemId))
                    .fetchFirst();
        }

    }

    @Override
    public Delivery get(String orderId) {
        return jpaRepository.findByOrderId(orderId).orElseThrow(()-> new CustomException(ErrorCode.DELIVERY_NOT_FOUND)).toModel();
    }
}
