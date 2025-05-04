package com.yzgeneration.evc.docs.usedItem;

import com.yzgeneration.evc.common.dto.SliceResponse;
import com.yzgeneration.evc.docs.RestDocsSupport;
import com.yzgeneration.evc.domain.item.enums.TransactionMode;
import com.yzgeneration.evc.domain.item.enums.TransactionStatus;
import com.yzgeneration.evc.domain.item.enums.TransactionType;
import com.yzgeneration.evc.domain.item.useditem.controller.UsedItemController;
import com.yzgeneration.evc.domain.item.useditem.dto.UsedItemsResponse.GetUsedItemsResponse;
import com.yzgeneration.evc.domain.item.useditem.dto.UsedItemRequest.CreateUsedItemRequest;
import com.yzgeneration.evc.domain.item.useditem.dto.UsedItemResponse.GetUsedItemResponse;
import com.yzgeneration.evc.domain.item.useditem.enums.ItemStatus;
import com.yzgeneration.evc.domain.item.useditem.service.UsedItemService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.SliceImpl;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static com.yzgeneration.evc.fixture.UsedItemFixture.fixCreateUsedItemRequest;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UsedItemControllerDocsTest extends RestDocsSupport {
    private final UsedItemService usedItemService = mock(UsedItemService.class);

    @Override
    protected Object initController() {
        return new UsedItemController(usedItemService);
    }

    @Test
    @DisplayName("중고상품 등록")
    void createUsedItem() throws Exception {

        CreateUsedItemRequest createUsedItemRequest = fixCreateUsedItemRequest();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/useditems")
                        .content(objectMapper.writeValueAsString(createUsedItemRequest))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("usedItem-create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
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
                                        .description("중고상품 거래유형 (DIRECT, DELIVERY)"),
                                fieldWithPath("transactionMode").type(JsonFieldType.STRING)
                                        .description("중고상품 거래방법 (SELL, BUY, AUCTION)"),
                                fieldWithPath("imageNames").type(JsonFieldType.ARRAY)
                                        .description("중고상품 이미지 이름 (image api의 response값에 있는 imageName")
                        ),
                        responseFields(
                                fieldWithPath("success").type(JsonFieldType.BOOLEAN)
                                        .description("성공여부")
                        )));
    }

    @Test
    @DisplayName("중고상품 전체 조회 (slice)")
    void getUsedItems() throws Exception {
        GetUsedItemsResponse getUsedItemsResponse = new GetUsedItemsResponse(1L, "title", 5000, TransactionMode.BUY, TransactionStatus.ONGOING, "imageName", 1L, LocalDateTime.MIN, ItemStatus.ACTIVE);
        SliceResponse<GetUsedItemsResponse> getUsedItemSliceResponse = new SliceResponse<>(new SliceImpl<>(List.of(getUsedItemsResponse), PageRequest.of(0, 10), true), LocalDateTime.MIN);

        when(usedItemService.getUsedItems(any()))
                .thenReturn(getUsedItemSliceResponse);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/useditems")
                        .param("cursor", LocalDateTime.MIN.toString()))
                .andExpect(status().isOk())
                .andDo(document("usedItems-get",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        queryParameters(parameterWithName("cursor").description("이전 메시지 조회를 위한 커서 (ISO-8601 형식: yyyy-MM-dd'T'HH:mm:ss)")),
                        responseFields(
                                fieldWithPath("content").type(JsonFieldType.ARRAY)
                                        .description("중고상품 정보 리스트"),
                                fieldWithPath("content[].usedItemId").type(JsonFieldType.NUMBER)
                                        .description("중고상품 id"),
                                fieldWithPath("content[].title").type(JsonFieldType.STRING)
                                        .description("중고상품 제목"),
                                fieldWithPath("content[].price").type(JsonFieldType.NUMBER)
                                        .description("중고상품 가격"),
                                fieldWithPath("content[].transactionMode").type(JsonFieldType.STRING)
                                        .description("중고상품 거래방법 (SELL, BUY, AUCTION)"),
                                fieldWithPath("content[].transactionStatus").type(JsonFieldType.STRING)
                                        .description("중고상품 거래 상태 (ONGOING, RESERVE, COMPLETE)"),
                                fieldWithPath("content[].imageName").type(JsonFieldType.STRING)
                                        .description("중고상품 이미지 (썸네일)"),
                                fieldWithPath("content[].likeCount").type(JsonFieldType.NUMBER)
                                        .description("게시물 좋아요수"),
                                fieldWithPath("content[].createAt").type(JsonFieldType.STRING)
                                        .description("중고상품 게시시간 (createAt과 현재시간과의 차이값을 프론트 화면에 렌더링)"),
                                fieldWithPath("content[].itemStatus").type(JsonFieldType.STRING)
                                        .description("중고상품 상태"),
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
    @DisplayName("개별 중고상품 조회 (상세페이지)")
    void getUsedItem() throws Exception {
        GetUsedItemResponse getUsedItemResponse = GetUsedItemResponse.builder()
                .title("title")
                .category("category")
                .content("content")
                .price(5000)
                .transactionType(TransactionType.DIRECT)
                .transactionMode(TransactionMode.BUY)
                .transactionStatus(TransactionStatus.ONGOING)
                .imageNames(List.of("imageName.jpg"))
                .viewCount(1L)
                .likeCount(1L)
                .chattingCount(1L)
                .marketMemberId(1L)
                .marketNickname("marketNickname")
                .isOwned(true)
                .createAt(LocalDateTime.MIN)
                .itemStatus(ItemStatus.ACTIVE)
                .build();

        when(usedItemService.getUsedItem(any(), any()))
                .thenReturn(getUsedItemResponse);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/useditems/{usedItemId}", 1L))
                .andExpect(status().isOk())
                .andDo(document("usedItem-get",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(parameterWithName("usedItemId").description("중고상품의 id")),
                        responseFields(
                                fieldWithPath("title").type(JsonFieldType.STRING)
                                        .description("중고상품 제목"),
                                fieldWithPath("category").type(JsonFieldType.STRING)
                                        .description("중고상품 카테고리"),
                                fieldWithPath("content").type(JsonFieldType.STRING)
                                        .description("중고상품 내용"),
                                fieldWithPath("price").type(JsonFieldType.NUMBER)
                                        .description("중고상품 가격"),
                                fieldWithPath("transactionType").type(JsonFieldType.STRING)
                                        .description("중고상품 거래유형 (DIRECT, DELIVERY)"),
                                fieldWithPath("transactionMode").type(JsonFieldType.STRING)
                                        .description("중고상품 거래방법 (SELL, BUY, AUCTION)"),
                                fieldWithPath("transactionStatus").type(JsonFieldType.STRING)
                                        .description("중고상품 거래상태 (ONGOING, RESERVE, COMPLETE)"),
                                fieldWithPath("imageNames").type(JsonFieldType.ARRAY)
                                        .description("중고상품 이미지 리스트"),
                                fieldWithPath("viewCount").type(JsonFieldType.NUMBER)
                                        .description("게시물 조회수"),
                                fieldWithPath("likeCount").type(JsonFieldType.NUMBER)
                                        .description("게시물 좋아요수"),
                                fieldWithPath("chattingCount").type(JsonFieldType.NUMBER)
                                        .description("게시물 채팅수"),
                                fieldWithPath("marketMemberId").type(JsonFieldType.NUMBER)
                                        .description("상점 주인의 id (상점 주인 상세페이지 이동에서 사용될 수 있으니 추가함)"),
                                fieldWithPath("marketNickname").type(JsonFieldType.STRING)
                                        .description("상점 주인 nickname"),
                                fieldWithPath("isOwned").type(JsonFieldType.BOOLEAN)
                                        .description("내가 작성한 글인지 유무"),
                                fieldWithPath("createAt").type(JsonFieldType.STRING)
                                        .description("중고상품 게시시간"),
                                fieldWithPath("itemStatus").type(JsonFieldType.STRING)
                                        .description("중고상품 게시상태 (ACTIVE, DELETED, BAN)")
                        )));
    }

    @Test
    @DisplayName("중고상품 검색 (slice)")
    void searchUsedItems() throws Exception {
        GetUsedItemsResponse getUsedItemsResponse = new GetUsedItemsResponse(1L, "title", 5000, TransactionMode.BUY, TransactionStatus.ONGOING, "imageName", 1L, LocalDateTime.MIN, ItemStatus.ACTIVE);
        SliceResponse<GetUsedItemsResponse> getUsedItemSliceResponse = new SliceResponse<>(new SliceImpl<>(List.of(getUsedItemsResponse), PageRequest.of(0, 10), true), LocalDateTime.MIN);

        when(usedItemService.searchUsedItems(any(), any()))
                .thenReturn(getUsedItemSliceResponse);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/useditems/search")
                        .param("q", "query")
                        .param("cursor", LocalDateTime.MIN.toString()))
                .andExpect(status().isOk())
                .andDo(document("usedItems-search",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        queryParameters(
                                parameterWithName("q").description("검색어"),
                                parameterWithName("cursor").description("이전 메시지 조회를 위한 커서 (ISO-8601 형식: yyyy-MM-dd'T'HH:mm:ss)")),
                        responseFields(
                                fieldWithPath("content").type(JsonFieldType.ARRAY)
                                        .description("중고상품 정보 리스트 (제목 또는 내용에 검색어가 포함된 모든 항목 조회 / 대소문자 구분 없음)"),
                                fieldWithPath("content[].usedItemId").type(JsonFieldType.NUMBER)
                                        .description("중고상품 id"),
                                fieldWithPath("content[].title").type(JsonFieldType.STRING)
                                        .description("중고상품 제목"),
                                fieldWithPath("content[].price").type(JsonFieldType.NUMBER)
                                        .description("중고상품 가격"),
                                fieldWithPath("content[].transactionMode").type(JsonFieldType.STRING)
                                        .description("중고상품 거래방법 (SELL, BUY, AUCTION)"),
                                fieldWithPath("content[].transactionStatus").type(JsonFieldType.STRING)
                                        .description("중고상품 거래 상태 (ONGOING, RESERVE, COMPLETE)"),
                                fieldWithPath("content[].imageName").type(JsonFieldType.STRING)
                                        .description("중고상품 이미지 (썸네일)"),
                                fieldWithPath("content[].likeCount").type(JsonFieldType.NUMBER)
                                        .description("게시물 좋아요수"),
                                fieldWithPath("content[].createAt").type(JsonFieldType.STRING)
                                        .description("중고상품 게시시간 (createAt과 현재시간과의 차이값을 프론트 화면에 렌더링)"),
                                fieldWithPath("content[].itemStatus").type(JsonFieldType.STRING)
                                        .description("중고상품 상태"),
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
}
