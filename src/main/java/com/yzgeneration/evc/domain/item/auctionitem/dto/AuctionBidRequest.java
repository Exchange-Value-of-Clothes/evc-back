package com.yzgeneration.evc.domain.item.auctionitem.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AuctionBidRequest {

    private Long auctionId;

    private int point;
}
