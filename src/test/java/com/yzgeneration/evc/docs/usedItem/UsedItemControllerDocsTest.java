package com.yzgeneration.evc.docs.usedItem;

import com.yzgeneration.evc.docs.RestDocsSupport;
import com.yzgeneration.evc.domain.useditem.controller.UsedItemController;
import com.yzgeneration.evc.domain.useditem.dto.UsedItemRequest.CreateUsedItemRequest;
import com.yzgeneration.evc.domain.useditem.dto.UsedItemResponse.CreateUsedItemResponse;
import com.yzgeneration.evc.domain.useditem.dto.UsedItemResponse.LoadUsedItemResponse;
import com.yzgeneration.evc.domain.useditem.dto.UsedItemResponse.LoadUsedItemsDetails;
import com.yzgeneration.evc.domain.useditem.dto.UsedItemResponse.LoadUsedItemsResponse;
import com.yzgeneration.evc.domain.useditem.enums.TransactionMode;
import com.yzgeneration.evc.domain.useditem.enums.TransactionStatue;
import com.yzgeneration.evc.domain.useditem.enums.TransactionType;
import com.yzgeneration.evc.domain.useditem.enums.UsedItemStatus;
import com.yzgeneration.evc.domain.useditem.service.UsedItemService;
import com.yzgeneration.evc.fixture.usedItem.UsedItemFixture;
import com.yzgeneration.evc.mock.usedItem.MockUsedItemImageFile;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UsedItemControllerDocsTest extends RestDocsSupport {
    private final UsedItemService usedItemService = mock(UsedItemService.class);

    @Override
    protected Object initController() {
        return new UsedItemController(usedItemService);
    }

    @Test
    @WithMockUser
    @DisplayName("중고상품 등록")
    void createUsedItem() throws Exception {

        CreateUsedItemRequest usedItem = UsedItemFixture.fixCreateUsedItemRequest();
        MockMultipartFile usedItemReq = new MockMultipartFile("createUsedItemRequest", "", "application/json", objectMapper.writeValueAsBytes(usedItem));

        CreateUsedItemResponse createUsedItemResponse = new CreateUsedItemResponse(usedItem.getMemberId(), 1L);

        when(usedItemService.createUsedItem(any(CreateUsedItemRequest.class), anyList()))
                .thenReturn(createUsedItemResponse);


        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/useditems")
                        .file(usedItemReq)
                        .file("imageFiles", new MockUsedItemImageFile().getImageFiles().get(0).getBytes())
                        .file("imageFiles", new MockUsedItemImageFile().getImageFiles().get(1).getBytes())
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().isOk())
                .andDo(document("usedItem-create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestPartFields("createUsedItemRequest",
                                fieldWithPath("memberId").type(JsonFieldType.NUMBER)
                                        .description("회원 Id"),
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
                                        .description("중고상품 거래방법 (SELL, BUY, AUCTION)")
                        ),
                        responseFields(
                                fieldWithPath("memberId").type(JsonFieldType.NUMBER)
                                        .description("회원의 id"),
                                fieldWithPath("usedItemId").type(JsonFieldType.NUMBER)
                                        .description("중고상품의 id")
                        )));
    }

    @Test
    @WithMockUser
    @DisplayName("중고상품들 조회 (메인페이지)")
    void loadUsedItems() throws Exception {
        LoadUsedItemsDetails loadUsedItemsDetails1 = LoadUsedItemsDetails.builder()
                .usedItemId(1L)
                .title("중고상품이요~")
                .price(10000)
                .transactionMode(TransactionMode.BUY)
                .imageURLs(List.of("https://domain/asdfasdfasdf"))
                .likeCount(0)
                .createAt(LocalDateTime.now())
                .usedItemStatus(UsedItemStatus.ACTIVE)
                .build();
        LoadUsedItemsDetails loadUsedItemsDetails2 = LoadUsedItemsDetails.builder()
                .usedItemId(2L)
                .title("중고상품이요~ 22")
                .price(20000)
                .transactionMode(TransactionMode.AUCTION)
                .imageURLs(List.of("https://domain/asdfasdfasdf"))
                .likeCount(0)
                .createAt(LocalDateTime.now())
                .usedItemStatus(UsedItemStatus.ACTIVE)
                .build();
        LoadUsedItemsResponse loadUsedItemsResponse = LoadUsedItemsResponse.builder()
                .loadUsedItemDetails(List.of(loadUsedItemsDetails1, loadUsedItemsDetails2))
                .isLast(false)
                .build();

        when(usedItemService.loadUsedItems(0))
                .thenReturn(loadUsedItemsResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/useditems")
                        .param("page", "0"))
                .andExpect(status().isOk())
                .andDo(document("usedItems-get",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        queryParameters(parameterWithName("page").description("출력 페이지 번호 / 0부터 시작 / 10개씩 뽑아줌")),
                        responseFields(
                                fieldWithPath("loadUsedItemDetails").type(JsonFieldType.ARRAY)
                                        .description("중고상품 리스트"),
                                fieldWithPath("loadUsedItemDetails[].usedItemId").type(JsonFieldType.NUMBER)
                                        .description("중고상품 id"),
                                fieldWithPath("loadUsedItemDetails[].title").type(JsonFieldType.STRING)
                                        .description("중고상품 제목"),
                                fieldWithPath("loadUsedItemDetails[].price").type(JsonFieldType.NUMBER)
                                        .description("중고상품 가격"),
                                fieldWithPath("loadUsedItemDetails[].transactionMode").type(JsonFieldType.STRING)
                                        .description("중고상품 거래방법 (SELL, BUY, AUCTION)"),
                                fieldWithPath("loadUsedItemDetails[].imageURLs").type(JsonFieldType.ARRAY)
                                        .description("중고상품 이미지 리스트"),
                                fieldWithPath("loadUsedItemDetails[].likeCount").type(JsonFieldType.NUMBER)
                                        .description("게시물 좋아요수"),
                                fieldWithPath("loadUsedItemDetails[].createAt").type(JsonFieldType.ARRAY)
                                        .description("중고상품 게시시간 (createAt과 현재시간과의 차이값을 프론트 화면에 렌더링)"),
                                fieldWithPath("loadUsedItemDetails[].usedItemStatus").type(JsonFieldType.STRING)
                                        .description("중고상품 상태"),
                                fieldWithPath("isLast").type(JsonFieldType.BOOLEAN)
                                        .description("페이지 마지막 유무 (false == 상품 더 있음 / true == 상품 없음")
                        )));
    }

    @Test
    @WithMockUser
    @DisplayName("개별 중고상품 조회 (상세페이지)")
    void loadUsedItem() throws Exception {
        LoadUsedItemResponse loadUsedItemResponse = LoadUsedItemResponse.builder()
                .title("중고상품이요")
                .category("윗도리")
                .content("강매는 아닌데 사십쇼.")
                .price(10000)
                .transactionType(TransactionType.DIRECT)
                .transactionMode(TransactionMode.BUY)
                .transactionStatue(TransactionStatue.ONGOING)
                .imageURLs(List.of("https://domain/asdfasdfasdf"))
                .viewCount(0)
                .likeCount(0)
                .chattingCount(0)
                .memberId(1L)
                .nickName("highyun")
                .isOwned(true)
                .createAt(LocalDateTime.now())
                .usedItemStatus(UsedItemStatus.ACTIVE)
                .build();

        when(usedItemService.loadUsedItem(any(), any()))
                .thenReturn(loadUsedItemResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/useditems/{usedItemId}", 1L)
                        .param("memberId", "0"))
                .andExpect(status().isOk())
                .andDo(document("usedItem-get",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(parameterWithName("usedItemId").description("중고상품의 id")),
                        queryParameters(parameterWithName("memberId").description("회원의 id")),
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
                                fieldWithPath("transactionStatue").type(JsonFieldType.STRING)
                                        .description("중고상품 거래상태 (ONGOING, RESERVE, COMPLETE)"),
                                fieldWithPath("imageURLs").type(JsonFieldType.ARRAY)
                                        .description("중고상품 이미지 리스트"),
                                fieldWithPath("viewCount").type(JsonFieldType.NUMBER)
                                        .description("게시물 조회수"),
                                fieldWithPath("likeCount").type(JsonFieldType.NUMBER)
                                        .description("게시물 좋아요수"),
                                fieldWithPath("chattingCount").type(JsonFieldType.NUMBER)
                                        .description("게시물 채팅수"),
                                fieldWithPath("memberId").type(JsonFieldType.NUMBER)
                                        .description("상점 주인의 id (상점 주인 상세페이지 이동에서 사용될 수 있으니 추가함)"),
                                fieldWithPath("nickName").type(JsonFieldType.STRING)
                                        .description("상점 주인 nickname"),
                                fieldWithPath("isOwned").type(JsonFieldType.BOOLEAN)
                                        .description("내가 작성한 글인지 유무"),
                                fieldWithPath("createAt").type(JsonFieldType.ARRAY)
                                        .description("중고상품 게시시간"),
                                fieldWithPath("usedItemStatus").type(JsonFieldType.STRING)
                                        .description("중고상품 게시상태 (ACTIVE, DELETED, BAN)")
                        )));
    }
}
