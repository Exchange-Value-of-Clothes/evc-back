package com.yzgeneration.evc.docs.my;

import com.yzgeneration.evc.common.dto.SliceResponse;
import com.yzgeneration.evc.docs.RestDocsSupport;
import com.yzgeneration.evc.domain.item.auctionitem.dto.AuctionItemsResponse.GetMyOrMemberAuctionItemsResponse;
import com.yzgeneration.evc.domain.item.auctionitem.dto.MyOrMemberAuctionItemsResponse;
import com.yzgeneration.evc.domain.item.auctionitem.service.AuctionItemService;
import com.yzgeneration.evc.domain.item.enums.TransactionMode;
import com.yzgeneration.evc.domain.item.enums.TransactionStatus;
import com.yzgeneration.evc.domain.item.useditem.enums.ItemStatus;
import com.yzgeneration.evc.domain.my.controller.MyAuctionItemController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.SliceImpl;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MyAuctionItemControllerDocsTest extends RestDocsSupport {
    private final AuctionItemService auctionItemService = mock(AuctionItemService.class);

    @Override
    protected Object initController() {
        return new MyAuctionItemController(auctionItemService);
    }

    @Test
    @DisplayName("내 경매상품 전체 조회")
    void getMyAuctionItems() throws Exception {
        GetMyOrMemberAuctionItemsResponse getMyOrMemberAuctionItemsResponse = new GetMyOrMemberAuctionItemsResponse(1L, "title", 5000, TransactionMode.BUY, TransactionStatus.ONGOING, "imageName", 1L, LocalDateTime.MIN, ItemStatus.ACTIVE);
        SliceResponse<GetMyOrMemberAuctionItemsResponse> getMyOrMemberAuctionItems = new SliceResponse<>(new SliceImpl<>(List.of(getMyOrMemberAuctionItemsResponse), PageRequest.of(0, 10), true), LocalDateTime.MIN);
        MyOrMemberAuctionItemsResponse myOrMemberauctionItems = new MyOrMemberAuctionItemsResponse(1L, getMyOrMemberAuctionItems);

        when(auctionItemService.getMyOrMemberAuctionItems(any(), any()))
                .thenReturn(myOrMemberauctionItems);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/my/auctionitems")
                        .param("cursor", LocalDateTime.MIN.toString()))
                .andExpect(status().isOk())
                .andDo(document("myAuctionItems-get",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        queryParameters(parameterWithName("cursor").description("이전 메시지 조회를 위한 커서 (ISO-8601 형식: yyyy-MM-dd'T'HH:mm:ss)")),
                        responseFields(
                                fieldWithPath("postItemCount").type(JsonFieldType.NUMBER)
                                        .description("전체 게시물수"),
                                fieldWithPath("myOrMemberAuctionItems.content").type(JsonFieldType.ARRAY)
                                        .description("내 경매상품 정보 리스트"),
                                fieldWithPath("myOrMemberAuctionItems.content[].auctionItemId").type(JsonFieldType.NUMBER)
                                        .description("경매상품 id"),
                                fieldWithPath("myOrMemberAuctionItems.content[].title").type(JsonFieldType.STRING)
                                        .description("경매상품 제목"),
                                fieldWithPath("myOrMemberAuctionItems.content[].price").type(JsonFieldType.NUMBER)
                                        .description("경매상품 가격"),
                                fieldWithPath("myOrMemberAuctionItems.content[].transactionMode").type(JsonFieldType.STRING)
                                        .description("경매상품 거래방법 (SELL, BUY, AUCTION)"),
                                fieldWithPath("myOrMemberAuctionItems.content[].transactionStatus").type(JsonFieldType.STRING)
                                        .description("경매상품 거래 상태 (ONGOING, RESERVE, COMPLETE)"),
                                fieldWithPath("myOrMemberAuctionItems.content[].imageName").type(JsonFieldType.STRING)
                                        .description("경매상품 이미지 (썸네일)"),
                                fieldWithPath("myOrMemberAuctionItems.content[].likeCount").type(JsonFieldType.NUMBER)
                                        .description("게시물 좋아요수"),
                                fieldWithPath("myOrMemberAuctionItems.content[].createAt").type(JsonFieldType.STRING)
                                        .description("경매상품 게시시간 (createAt과 현재시간과의 차이값을 프론트 화면에 렌더링)"),
                                fieldWithPath("myOrMemberAuctionItems.content[].itemStatus").type(JsonFieldType.STRING)
                                        .description("경매상품 상태"),
                                fieldWithPath("myOrMemberAuctionItems.hasNext").type(JsonFieldType.BOOLEAN)
                                        .description("다음 페이지 존재여부"),
                                fieldWithPath("myOrMemberAuctionItems.size").type(JsonFieldType.NUMBER)
                                        .description("페이지 요청 사이즈"),
                                fieldWithPath("myOrMemberAuctionItems.numberOfElements").type(JsonFieldType.NUMBER)
                                        .description("조회된 데이터 개수"),
                                fieldWithPath("myOrMemberAuctionItems.cursor").type(JsonFieldType.STRING)
                                        .description("다음 페이지 커서")
                        )));

    }
}
