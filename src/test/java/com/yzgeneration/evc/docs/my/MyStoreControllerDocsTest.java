package com.yzgeneration.evc.docs.my;

import com.yzgeneration.evc.common.dto.SliceResponse;
import com.yzgeneration.evc.docs.RestDocsSupport;
import com.yzgeneration.evc.domain.item.auctionitem.dto.AuctionItemsResponse.GetMyOrMemberAuctionItemsResponse;
import com.yzgeneration.evc.domain.item.auctionitem.dto.MyOrMemberAuctionItemsResponse;
import com.yzgeneration.evc.domain.item.auctionitem.service.AuctionItemService;
import com.yzgeneration.evc.domain.item.enums.TransactionMode;
import com.yzgeneration.evc.domain.item.enums.TransactionStatus;
import com.yzgeneration.evc.domain.item.useditem.dto.MyOrMemberUsedItemsResponse;
import com.yzgeneration.evc.domain.item.useditem.dto.UsedItemsResponse.GetMyOrMemberUsedItemsResponse;
import com.yzgeneration.evc.domain.item.useditem.enums.ItemStatus;
import com.yzgeneration.evc.domain.item.useditem.service.UsedItemService;
import com.yzgeneration.evc.domain.my.controller.MyStoreController;
import com.yzgeneration.evc.domain.my.dto.MyAuctionItemUpdateRequest;
import com.yzgeneration.evc.domain.my.dto.MyUsedItemUpdateRequest;
import com.yzgeneration.evc.domain.my.service.MyStoreService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.SliceImpl;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MyStoreControllerDocsTest extends RestDocsSupport {
    private final MyStoreService myStoreService = mock(MyStoreService.class);
    private final UsedItemService usedItemService = mock(UsedItemService.class);
    private final AuctionItemService auctionItemService = mock(AuctionItemService.class);

    @Override
    protected Object initController() {
        return new MyStoreController(myStoreService, usedItemService, auctionItemService);
    }

    @Test
    @DisplayName("내 중고상품 전체 조회")
    void getMyUsedItems() throws Exception {
        GetMyOrMemberUsedItemsResponse getMyOrMemberUsedItemsResponse = new GetMyOrMemberUsedItemsResponse(1L, "title", 5000, TransactionMode.BUY, TransactionStatus.ONGOING, "imageName", 1L, LocalDateTime.MIN, ItemStatus.ACTIVE);
        SliceResponse<GetMyOrMemberUsedItemsResponse> getMyOrMemberUsedItems = new SliceResponse<>(new SliceImpl<>(List.of(getMyOrMemberUsedItemsResponse), PageRequest.of(0, 10), true), LocalDateTime.MIN);
        MyOrMemberUsedItemsResponse myOrMemberUsedItems = new MyOrMemberUsedItemsResponse(1L, getMyOrMemberUsedItems);

        when(myStoreService.getMyUsedItems(any(), any(), any()))
                .thenReturn(myOrMemberUsedItems);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/my/store/useditems")
                        .param("cursor", LocalDateTime.MIN.toString())
                        .param("condition", TransactionMode.BUY.name()))
                .andExpect(status().isOk())
                .andDo(document("my-useditems-get",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        queryParameters(
                                parameterWithName("cursor").description("이전 중고상품 조회를 위한 커서 (ISO-8601 형식: yyyy-MM-dd'T'HH:mm:ss)"),
                                parameterWithName("condition").description("중고상품의 TransactionMode (공백, BUY, SELL / 공백시 전체 조회)")),
                        responseFields(
                                fieldWithPath("postItemCount").type(JsonFieldType.NUMBER)
                                        .description("전체 게시물수"),
                                fieldWithPath("myOrMemberUsedItems.content").type(JsonFieldType.ARRAY)
                                        .description("내 중고상품 정보 리스트"),
                                fieldWithPath("myOrMemberUsedItems.content[].usedItemId").type(JsonFieldType.NUMBER)
                                        .description("중고상품 id"),
                                fieldWithPath("myOrMemberUsedItems.content[].title").type(JsonFieldType.STRING)
                                        .description("중고상품 제목"),
                                fieldWithPath("myOrMemberUsedItems.content[].price").type(JsonFieldType.NUMBER)
                                        .description("중고상품 가격"),
                                fieldWithPath("myOrMemberUsedItems.content[].transactionMode").type(JsonFieldType.STRING)
                                        .description("중고상품 거래방법 (SELL, BUY, AUCTION)"),
                                fieldWithPath("myOrMemberUsedItems.content[].transactionStatus").type(JsonFieldType.STRING)
                                        .description("중고상품 거래 상태 (ONGOING, RESERVE, COMPLETE)"),
                                fieldWithPath("myOrMemberUsedItems.content[].imageName").type(JsonFieldType.STRING)
                                        .description("중고상품 이미지 (썸네일)"),
                                fieldWithPath("myOrMemberUsedItems.content[].likeCount").type(JsonFieldType.NUMBER)
                                        .description("게시물 좋아요수"),
                                fieldWithPath("myOrMemberUsedItems.content[].createAt").type(JsonFieldType.STRING)
                                        .description("중고상품 게시시간 (createAt과 현재시간과의 차이값을 프론트 화면에 렌더링)"),
                                fieldWithPath("myOrMemberUsedItems.content[].itemStatus").type(JsonFieldType.STRING)
                                        .description("중고상품 상태"),
                                fieldWithPath("myOrMemberUsedItems.hasNext").type(JsonFieldType.BOOLEAN)
                                        .description("다음 페이지 존재여부"),
                                fieldWithPath("myOrMemberUsedItems.size").type(JsonFieldType.NUMBER)
                                        .description("페이지 요청 사이즈"),
                                fieldWithPath("myOrMemberUsedItems.numberOfElements").type(JsonFieldType.NUMBER)
                                        .description("조회된 데이터 개수"),
                                fieldWithPath("myOrMemberUsedItems.cursor").type(JsonFieldType.STRING)
                                        .description("다음 페이지 커서")
                        )));
    }

    @Test
    @DisplayName("내 경매상품 전체 조회")
    void getMyAuctionItems() throws Exception {
        GetMyOrMemberAuctionItemsResponse getMyOrMemberAuctionItemsResponse = new GetMyOrMemberAuctionItemsResponse(1L, "title", 5000, TransactionMode.BUY, TransactionStatus.ONGOING, "imageName", 1L, LocalDateTime.MIN, ItemStatus.ACTIVE);
        SliceResponse<GetMyOrMemberAuctionItemsResponse> getMyOrMemberAuctionItems = new SliceResponse<>(new SliceImpl<>(List.of(getMyOrMemberAuctionItemsResponse), PageRequest.of(0, 10), true), LocalDateTime.MIN);
        MyOrMemberAuctionItemsResponse myOrMemberauctionItems = new MyOrMemberAuctionItemsResponse(1L, getMyOrMemberAuctionItems);

        when(myStoreService.getMyAuctionItems(any(), any()))
                .thenReturn(myOrMemberauctionItems);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/my/store/auctionitems")
                        .param("cursor", LocalDateTime.MIN.toString()))
                .andExpect(status().isOk())
                .andDo(document("my-auctionitems-get",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        queryParameters(parameterWithName("cursor").description("이전 경매상품 조회를 위한 커서 (ISO-8601 형식: yyyy-MM-dd'T'HH:mm:ss)")),
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

    @Test
    @DisplayName("중고상품의 transaction status 변경")
    void updateUsedItemTransactionStatus() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch("/api/my/store/useditems/{usedItemId}", 1L)
                        .queryParam("transactionStatus", TransactionStatus.RESERVE.name()))
                .andExpect(status().isOk())
                .andDo(document("my-useditem-transactionstatus-update",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(parameterWithName("usedItemId").description("중고상품의 id")),
                        queryParameters(parameterWithName("transactionStatus").description("변경할 Transaction Status (ONGOING, RESERVE, COMPLETE)")),
                        responseFields(
                                fieldWithPath("success").type(JsonFieldType.BOOLEAN)
                                        .description("성공여부")
                        )));
    }

    @Test
    @DisplayName("중고상품 삭제")
    void deleteUsedItem() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/my/store/useditems/{usedItemId}", 1L))
                .andExpect(status().isOk())
                .andDo(document("my-useditem-delete",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(parameterWithName("usedItemId").description("중고상품의 id")),
                        responseFields(
                                fieldWithPath("success").type(JsonFieldType.BOOLEAN)
                                        .description("성공여부")
                        )));
    }

    @Test
    @DisplayName("경매상품 삭제")
    void deleteAuctionItem() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/my/store/auctionitems/{auctionItemId}", 1L))
                .andExpect(status().isOk())
                .andDo(document("my-auctionitem-delete",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(parameterWithName("auctionItemId").description("경매상품의 id")),
                        responseFields(
                                fieldWithPath("success").type(JsonFieldType.BOOLEAN)
                                        .description("성공여부")
                        )));
    }

    @Test
    @DisplayName("내 중고상품 수정")
    void putMyUsedItem() throws Exception {

        MyUsedItemUpdateRequest myUsedItemUpdateRequest = new MyUsedItemUpdateRequest("title", "category", "content", 5000, "DIRECT", "SELL", List.of("imageName"));

        mockMvc.perform(MockMvcRequestBuilders.put("/api/my/store/useditems/{usedItemId}", 1L)
                        .content(objectMapper.writeValueAsString(myUsedItemUpdateRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("my-useditem-put",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(parameterWithName("usedItemId").description("중고상품 id")),
                        requestFields(
                                fieldWithPath("title").type(JsonFieldType.STRING)
                                        .description("중고상품 제목"),
                                fieldWithPath("category").type(JsonFieldType.STRING)
                                        .description("중고상품 카테고리"),
                                fieldWithPath("content").type(JsonFieldType.STRING)
                                        .description("중고상품 내용"),
                                fieldWithPath("price").type(JsonFieldType.NUMBER)
                                        .description("중고상품 가격"),
                                fieldWithPath("transactionType").type(JsonFieldType.STRING)
                                        .description("중고상품 거래유행 (DIRECT, DELIVERY)"),
                                fieldWithPath("transactionMode").type(JsonFieldType.STRING)
                                        .description("중고상품 거래방법 (SELL, BUY)"),
                                fieldWithPath("imageNames").type(JsonFieldType.ARRAY)
                                        .description("중고상품 이미지 리스트")
                        ),
                        responseFields(
                                fieldWithPath("success").type(JsonFieldType.BOOLEAN)
                                        .description("성공여부")
                        )));
    }

    @Test
    @DisplayName("내 경매상품 수정")
    void putMyAuctionItem() throws Exception {

        MyAuctionItemUpdateRequest myAuctionItemUpdateRequest = new MyAuctionItemUpdateRequest("title", "category", "content", 5000, 1000, "DIRECT", List.of("imageName"));

        mockMvc.perform(MockMvcRequestBuilders.put("/api/my/store/auctionitems/{auctionItemId}", 1L)
                        .content(objectMapper.writeValueAsString(myAuctionItemUpdateRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("my-auctionitem-put",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(parameterWithName("auctionItemId").description("경매상품 id")),
                        requestFields(
                                fieldWithPath("title").type(JsonFieldType.STRING)
                                        .description("경매상품 제목"),
                                fieldWithPath("category").type(JsonFieldType.STRING)
                                        .description("경매상품 카테고리"),
                                fieldWithPath("content").type(JsonFieldType.STRING)
                                        .description("경매상품 내용"),
                                fieldWithPath("startPrice").type(JsonFieldType.NUMBER)
                                        .description("경매상품 시가 (경매 참여자가 있는 경우, 0 또는 기존 값으로 보내기)"),
                                fieldWithPath("bidPrice").type(JsonFieldType.NUMBER)
                                        .description("경매상품 호가"),
                                fieldWithPath("transactionType").type(JsonFieldType.STRING)
                                        .description("경매상품 거래유행 (DIRECT, DELIVERY)"),
                                fieldWithPath("imageNames").type(JsonFieldType.ARRAY)
                                        .description("경매상품 이미지 리스트")
                        ),
                        responseFields(
                                fieldWithPath("success").type(JsonFieldType.BOOLEAN)
                                        .description("성공여부")
                        )));
    }
}
