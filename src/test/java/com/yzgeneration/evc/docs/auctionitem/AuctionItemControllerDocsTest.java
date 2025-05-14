package com.yzgeneration.evc.docs.auctionitem;

import com.yzgeneration.evc.common.dto.SliceResponse;
import com.yzgeneration.evc.docs.RestDocsSupport;
import com.yzgeneration.evc.domain.item.auctionitem.controller.AuctionItemController;
import com.yzgeneration.evc.domain.item.auctionitem.dto.AuctionItemRequest.CreateAuctionItemRequest;
import com.yzgeneration.evc.domain.item.auctionitem.dto.AuctionItemResponse.AuctionItemDetailsResponse;
import com.yzgeneration.evc.domain.item.auctionitem.dto.AuctionItemResponse.AuctionItemStatsResponse;
import com.yzgeneration.evc.domain.item.auctionitem.dto.AuctionItemResponse.GetAuctionItemResponse;
import com.yzgeneration.evc.domain.item.auctionitem.dto.AuctionItemsResponse.AuctionItemPriceDetailResponse;
import com.yzgeneration.evc.domain.item.auctionitem.dto.AuctionItemsResponse.GetAuctionItemsResponse;
import com.yzgeneration.evc.domain.item.auctionitem.service.AuctionItemService;
import com.yzgeneration.evc.domain.item.enums.TransactionType;
import com.yzgeneration.evc.domain.item.useditem.enums.ItemStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.SliceImpl;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static com.yzgeneration.evc.fixture.AuctionItemFixture.fixCreateAuctionItemRequest;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuctionItemControllerDocsTest extends RestDocsSupport {
    private final AuctionItemService auctionItemService = mock(AuctionItemService.class);

    @Override
    protected Object initController() {
        return new AuctionItemController(auctionItemService);
    }

    @Test
    @DisplayName("경매상품 등록")
    void createAuctionItem() throws Exception {

        CreateAuctionItemRequest createAuctionItemRequest = fixCreateAuctionItemRequest();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auctionitems")
                        .content(objectMapper.writeValueAsString(createAuctionItemRequest))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("auctionitem-create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("title").type(JsonFieldType.STRING)
                                        .description("경매상품 제목"),
                                fieldWithPath("transactionType").type(JsonFieldType.STRING)
                                        .description("경매상품 거래유행 (DIRECT, DELIVERY)"),
                                fieldWithPath("category").type(JsonFieldType.STRING)
                                        .description("경매상품 카테고리"),
                                fieldWithPath("content").type(JsonFieldType.STRING)
                                        .description("경매상품 내용"),
                                fieldWithPath("startPrice").type(JsonFieldType.NUMBER)
                                        .description("경매상품 시가"),
                                fieldWithPath("bidPrice").type(JsonFieldType.NUMBER)
                                        .description("경매상품 호가"),
                                fieldWithPath("imageNames").type(JsonFieldType.ARRAY)
                                        .description("경매상품 이미지 리스트")

                        ),
                        responseFields(
                                fieldWithPath("success").type(JsonFieldType.BOOLEAN)
                                        .description("성공여부")
                        )));
    }

    @Test
    @DisplayName("경매상품 전체 조회 (Slice)")
    void getAuctionItems() throws Exception {

        AuctionItemPriceDetailResponse auctionItemPriceDetailResponse = new AuctionItemPriceDetailResponse(5000, 5000, 1000);
        GetAuctionItemsResponse getAuctionItemsResponse = new GetAuctionItemsResponse(1L, "title", "category", auctionItemPriceDetailResponse, 1L, "imageNamge.jpg", LocalDateTime.MIN, LocalDateTime.MIN.plusDays(1), 1000);
        SliceResponse<GetAuctionItemsResponse> getAuctionItemSliceResponse = new SliceResponse<>(new SliceImpl<>(List.of(getAuctionItemsResponse), PageRequest.of(0, 10), true), LocalDateTime.MIN);

        when(auctionItemService.getAuctionItems(any(), any()))
                .thenReturn(getAuctionItemSliceResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/auctionitems")
                        .param("cursor", LocalDateTime.MIN.toString()))
                .andExpect(status().isOk())
                .andDo(document("auctionitems-get",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        queryParameters(parameterWithName("cursor").description("이전 메시지 조회를 위한 커서 (ISO-8601 형식: yyyy-MM-dd'T'HH:mm:ss)")),
                        responseFields(
                                fieldWithPath("content").type(JsonFieldType.ARRAY)
                                        .description("경매상품 정보 리스트"),
                                fieldWithPath("content[].auctionItemId").type(JsonFieldType.NUMBER)
                                        .description("경매상품 id"),
                                fieldWithPath("content[].title").type(JsonFieldType.STRING)
                                        .description("경매상품 제목"),
                                fieldWithPath("content[].category").type(JsonFieldType.STRING)
                                        .description("경매상품 카테고리"),
                                fieldWithPath("content[].startPrice").type(JsonFieldType.NUMBER)
                                        .description("경매상품 시가"),
                                fieldWithPath("content[].currentPrice").type(JsonFieldType.NUMBER)
                                        .description("경매상품 현재 가격"),
                                fieldWithPath("content[].bidPrice").type(JsonFieldType.NUMBER)
                                        .description("경매상품 호가"),
                                fieldWithPath("content[].participantCount").type(JsonFieldType.NUMBER)
                                        .description("경매상품 경매 참여자수"),
                                fieldWithPath("content[].imageName").type(JsonFieldType.STRING)
                                        .description("경매상품 이미지 (썸네일)"),
                                fieldWithPath("content[].startTime").type(JsonFieldType.STRING)
                                        .description("경매 시작 시간"),
                                fieldWithPath("content[].endTime").type(JsonFieldType.STRING)
                                        .description("경매 종료 시간 (시작 시간 + 24시간 / 남은 시간 : end - start)"),
                                fieldWithPath("content[].point").type(JsonFieldType.NUMBER)
                                        .description("회원의 포인트"),
                                fieldWithPath("hasNext").type(JsonFieldType.BOOLEAN)
                                        .description("다음 페이지 존재여부"),
                                fieldWithPath("size").type(JsonFieldType.NUMBER)
                                        .description("페이지 요청 사이즈"),
                                fieldWithPath("numberOfElements").type(JsonFieldType.NUMBER)
                                        .description("조회된 데이터 개수"),
                                fieldWithPath("cursor").type(JsonFieldType.STRING)
                                        .description("다음 페이지 커서")
                        )));
    }

    @Test
    @DisplayName("경매상품 조회")
    void getAuctionItem() throws Exception {

        AuctionItemDetailsResponse auctionItemDetailsResponse = new AuctionItemDetailsResponse("title", "category", "content");
        AuctionItemStatsResponse auctionItemStatsResponse = new AuctionItemStatsResponse(1L, 1L, 1L);
        List<String> imageNameList = List.of("imageName.jpg");
        GetAuctionItemResponse getAuctionItemResponse = new GetAuctionItemResponse(auctionItemDetailsResponse, auctionItemStatsResponse, imageNameList, TransactionType.DIRECT, LocalDateTime.MIN, LocalDateTime.MIN.plusDays(1), 5000, 1L, "marketNickname", false, ItemStatus.ACTIVE);

        when(auctionItemService.getAuctionItem(any(), any()))
                .thenReturn(getAuctionItemResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/auctionitems/{auctionItemId}", 1L))
                .andExpect(status().isOk())
                .andDo(document("auctionitem-get",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(parameterWithName("auctionItemId").description("경매상품의 id")),
                        responseFields(
                                fieldWithPath("title").type(JsonFieldType.STRING)
                                        .description("경매상품 제목"),
                                fieldWithPath("category").type(JsonFieldType.STRING)
                                        .description("경매상품 카테고리"),
                                fieldWithPath("content").type(JsonFieldType.STRING)
                                        .description("경매상품 내용"),
                                fieldWithPath("viewCount").type(JsonFieldType.NUMBER)
                                        .description("경매상품 조회수"),
                                fieldWithPath("likeCount").type(JsonFieldType.NUMBER)
                                        .description("경매상품 좋아요수"),
                                fieldWithPath("participantCount").type(JsonFieldType.NUMBER)
                                        .description("경매상품 경매 참여자수"),
                                fieldWithPath("imageNameList").type(JsonFieldType.ARRAY)
                                        .description("경매상품 이미지 리스트"),
                                fieldWithPath("transactionType").type(JsonFieldType.STRING)
                                        .description("중고상품 거래유형 (DIRECT, DELIVERY)"),
                                fieldWithPath("startTime").type(JsonFieldType.STRING)
                                        .description("경매 시작 시간"),
                                fieldWithPath("endTime").type(JsonFieldType.STRING)
                                        .description("경매 종료 시간"),
                                fieldWithPath("currentPrice").type(JsonFieldType.NUMBER)
                                        .description("경매상품 시가"),
                                fieldWithPath("marketMemberId").type(JsonFieldType.NUMBER)
                                        .description("상점 주인의 id (상점 주인 상세페이지 이동에서 사용될 수 있으니 추가함)"),
                                fieldWithPath("marketNickname").type(JsonFieldType.STRING)
                                        .description("상점 주인 nickname"),
                                fieldWithPath("isOwned").type(JsonFieldType.BOOLEAN)
                                        .description("내가 작성한 글인지 유무"),
                                fieldWithPath("itemStatus").type(JsonFieldType.STRING)
                                        .description("중고상품 게시상태 (ACTIVE, DELETED, BAN)")
                        )));
    }

    @Test
    @DisplayName("경매상품 검색 (Slice)")
    void searchAuctionItems() throws Exception {

        AuctionItemPriceDetailResponse auctionItemPriceDetailResponse = new AuctionItemPriceDetailResponse(5000, 5000, 1000);
        GetAuctionItemsResponse getAuctionItemsResponse = new GetAuctionItemsResponse(1L, "title", "category", auctionItemPriceDetailResponse, 0L, "imageNamge.jpg", LocalDateTime.MIN, LocalDateTime.MIN.plusDays(1), 1000);
        SliceResponse<GetAuctionItemsResponse> getAuctionItemSliceResponse = new SliceResponse<>(new SliceImpl<>(List.of(getAuctionItemsResponse), PageRequest.of(0, 10), true), LocalDateTime.MIN);

        when(auctionItemService.searchAuctionItems(any(), any(), any()))
                .thenReturn(getAuctionItemSliceResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/auctionitems/search")
                        .param("q", "query")
                        .param("cursor", LocalDateTime.MIN.toString()))
                .andExpect(status().isOk())
                .andDo(document("auctionitems-search",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        queryParameters(
                                parameterWithName("q").description("검색어"),
                                parameterWithName("cursor").description("이전 메시지 조회를 위한 커서 (ISO-8601 형식: yyyy-MM-dd'T'HH:mm:ss)")),
                        responseFields(
                                fieldWithPath("content").type(JsonFieldType.ARRAY)
                                        .description("경매상품 정보 리스트 (제목 또는 내용에 검색어가 포함된 모든 항목 조회 / 대소문자 구분 없음)"),
                                fieldWithPath("content[].auctionItemId").type(JsonFieldType.NUMBER)
                                        .description("경매상품 id"),
                                fieldWithPath("content[].title").type(JsonFieldType.STRING)
                                        .description("경매상품 제목"),
                                fieldWithPath("content[].category").type(JsonFieldType.STRING)
                                        .description("경매상품 카테고리"),
                                fieldWithPath("content[].startPrice").type(JsonFieldType.NUMBER)
                                        .description("경매상품 시가"),
                                fieldWithPath("content[].currentPrice").type(JsonFieldType.NUMBER)
                                        .description("경매상품 현재 가격"),
                                fieldWithPath("content[].bidPrice").type(JsonFieldType.NUMBER)
                                        .description("경매상품 호가"),
                                fieldWithPath("content[].participantCount").type(JsonFieldType.NUMBER)
                                        .description("경매상품 경매 참여자수"),
                                fieldWithPath("content[].imageName").type(JsonFieldType.STRING)
                                        .description("경매상품 이미지 (썸네일)"),
                                fieldWithPath("content[].startTime").type(JsonFieldType.STRING)
                                        .description("경매 시작 시간"),
                                fieldWithPath("content[].endTime").type(JsonFieldType.STRING)
                                        .description("경매 종료 시간 (시작 시간 + 24시간 / 남은 시간 : end - start)"),
                                fieldWithPath("content[].point").type(JsonFieldType.NUMBER)
                                        .description("회원의 포인트"),
                                fieldWithPath("hasNext").type(JsonFieldType.BOOLEAN)
                                        .description("다음 페이지 존재여부"),
                                fieldWithPath("size").type(JsonFieldType.NUMBER)
                                        .description("페이지 요청 사이즈"),
                                fieldWithPath("numberOfElements").type(JsonFieldType.NUMBER)
                                        .description("조회된 데이터 개수"),
                                fieldWithPath("cursor").type(JsonFieldType.STRING)
                                        .description("다음 페이지 커서")
                        )));
    }

    @Test
    @DisplayName("경매상품 삭제")
    void deleteAuctionItem() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/auctionitems/{auctionItemId}", 1L))
                .andExpect(status().isOk())
                .andDo(document("auctionitem-delete",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(parameterWithName("auctionItemId").description("경매상품의 id")),
                        responseFields(
                                fieldWithPath("success").type(JsonFieldType.BOOLEAN)
                                        .description("성공여부")
                        )));
    }
}
