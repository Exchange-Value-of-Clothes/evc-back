package com.yzgeneration.evc.domain.item.auctionitem.infrastructure.entity;

import com.yzgeneration.evc.domain.item.auctionitem.model.AuctionRoom;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "auction_rooms")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AuctionRoomEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long auctionItemId;

    private LocalDateTime createdAt;

    private Boolean isDeleted;

    public static AuctionRoomEntity from(AuctionRoom auctionRoom) {
        return AuctionRoomEntity.builder()
                .auctionItemId(auctionRoom.getAuctionItemId())
                .isDeleted(auctionRoom.getIsDeleted())
                .build();
    }

    public AuctionRoom toModel() {
        return AuctionRoom.builder()
                .id(id)
                .auctionItemId(auctionItemId)
                .isDeleted(isDeleted)
                .build();
    }
}
