package com.yzgeneration.evc.domain.item.auctionitem.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AuctionBidToListener {

    private Long auctionRoomId;

    private Long bidderId;

    private int currentPrice;
}
