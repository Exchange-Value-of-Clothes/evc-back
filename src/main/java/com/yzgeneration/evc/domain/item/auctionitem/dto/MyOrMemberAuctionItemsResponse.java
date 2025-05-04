package com.yzgeneration.evc.domain.item.auctionitem.dto;

import com.yzgeneration.evc.common.dto.SliceResponse;
import com.yzgeneration.evc.domain.item.auctionitem.dto.AuctionItemsResponse.GetMyOrMemberAuctionItemsResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MyOrMemberAuctionItemsResponse {

    private Long postItemCount;

    private SliceResponse<GetMyOrMemberAuctionItemsResponse> myOrMemberAuctionItems;
}
