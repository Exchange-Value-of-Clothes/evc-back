package com.yzgeneration.evc.domain.delivery.infrastructure;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yzgeneration.evc.common.dto.SliceResponse;
import com.yzgeneration.evc.domain.delivery.dto.MobilityCreate;
import com.yzgeneration.evc.domain.delivery.model.Delivery;
import com.yzgeneration.evc.domain.delivery.model.DeliveryView;
import com.yzgeneration.evc.domain.image.enums.ItemType;

import com.yzgeneration.evc.domain.member.infrastructure.QMemberEntity;
import com.yzgeneration.evc.exception.CustomException;
import com.yzgeneration.evc.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.yzgeneration.evc.domain.delivery.infrastructure.QDeliveryEntity.*;
import static com.yzgeneration.evc.domain.delivery.infrastructure.QDeliveryViewEntity.deliveryViewEntity;
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
    public MobilityCreate createInfo(String orderId, ItemType itemType, Long itemId) {
        QMemberEntity sender = new QMemberEntity("sender");
        QMemberEntity receiver = new QMemberEntity("receiver");
        QAddressEntity senderAddress = new QAddressEntity("senderAddress");
        QAddressEntity receiverAddress = new QAddressEntity("receiverAddress");

        if (itemType == ItemType.USEDITEM) {
            return queryFactory.select(Projections.constructor(MobilityCreate.class,
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
            return queryFactory.select(Projections.constructor(MobilityCreate.class,
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

    @Override
    public SliceResponse<DeliveryView> findList(Long memberId, LocalDateTime cursor) {
        int size = 10;
        BooleanBuilder where = new BooleanBuilder();
        where.and(deliveryViewEntity.memberId.eq(memberId));
        if (cursor != null) {
            where.and(deliveryViewEntity.createdAt.lt(cursor));
        }

        List<DeliveryView> response = new ArrayList<>(queryFactory
                .selectFrom(deliveryViewEntity)
                .where(where)
                .orderBy(deliveryViewEntity.createdAt.desc())
                .limit(size + 1)
                .fetch()
                .stream().map(DeliveryViewEntity::toModel).toList());


        boolean hasNext = response.size() > size;
        if (hasNext) response.remove(size); // 커서만 넘기기 위해 마지막 항목 제거
        LocalDateTime lastCreatedAt = !response.isEmpty()
                ? response.get(response.size() - 1).getCreatedAt()
                : null;
        return new SliceResponse<>(
                new SliceImpl<>(response, PageRequest.of(0, size), hasNext), lastCreatedAt
        );
    }


}
