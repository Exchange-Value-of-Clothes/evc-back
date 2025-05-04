package com.yzgeneration.evc.domain.item.useditem.dto;

import com.yzgeneration.evc.common.dto.SliceResponse;
import com.yzgeneration.evc.domain.item.useditem.dto.UsedItemsResponse.GetMyOrMemberUsedItemsResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MyOrMemberUsedItemsResponse {

    private Long postItemCount;

    private SliceResponse<GetMyOrMemberUsedItemsResponse> myOrMemberUsedItems;
}
