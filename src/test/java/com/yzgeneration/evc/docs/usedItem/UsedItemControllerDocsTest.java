package com.yzgeneration.evc.docs.usedItem;

import com.yzgeneration.evc.docs.RestDocsSupport;
import com.yzgeneration.evc.domain.item.useditem.controller.UsedItemController;
import com.yzgeneration.evc.domain.item.useditem.dto.UsedItemRequest.CreateUsedItemRequest;
import com.yzgeneration.evc.domain.item.useditem.dto.UsedItemResponse.GetUsedItemResponse;
import com.yzgeneration.evc.domain.item.useditem.dto.UsedItemResponse.GetUsedItemsDetails;
import com.yzgeneration.evc.domain.item.useditem.dto.UsedItemResponse.GetUsedItemsResponse;
import com.yzgeneration.evc.domain.item.useditem.enums.ItemStatus;
import com.yzgeneration.evc.domain.item.enums.TransactionMode;
import com.yzgeneration.evc.domain.item.enums.TransactionStatus;
import com.yzgeneration.evc.domain.item.enums.TransactionType;
import com.yzgeneration.evc.domain.item.useditem.service.UsedItemService;
import com.yzgeneration.evc.fixture.MemberFixture;
import com.yzgeneration.evc.security.MemberPrincipal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
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
        MemberPrincipal memberPrincipal = new MemberPrincipal(MemberFixture.withFakeUser());
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                memberPrincipal,
                memberPrincipal.getMember().getId(),
                memberPrincipal.getAuthorities());
        CreateUsedItemRequest createUsedItemRequest = fixCreateUsedItemRequest();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/useditems")
                        .content(objectMapper.writeValueAsString(createUsedItemRequest))
                        .with(csrf())
                        .with(SecurityMockMvcRequestPostProcessors.authentication(authentication))
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
    @DisplayName("중고상품들 조회 (메인페이지)")
    void loadUsedItems() throws Exception {
        GetUsedItemsDetails getUsedItemsDetails1 = GetUsedItemsDetails.builder()
                .usedItemId(1L)
                .title("중고상품이요~")
                .price(10000)
                .transactionMode(TransactionMode.BUY)
                .transactionStatus(TransactionStatus.ONGOING)
                .imageName("https://domain/image/1234.jpg")
                .likeCount(0)
                .createAt(LocalDateTime.now())
                .itemStatus(ItemStatus.ACTIVE)
                .build();
        GetUsedItemsDetails getUsedItemsDetails2 = GetUsedItemsDetails.builder()
                .usedItemId(2L)
                .title("중고상품이요~ 22")
                .price(20000)
                .transactionMode(TransactionMode.SELL)
                .transactionStatus(TransactionStatus.ONGOING)
                .imageName("https://domain/image/5678.jpg")
                .likeCount(0)
                .createAt(LocalDateTime.now())
                .itemStatus(ItemStatus.ACTIVE)
                .build();
        GetUsedItemsResponse getUsedItemsResponse = GetUsedItemsResponse.builder()
                .loadUsedItemDetails(List.of(getUsedItemsDetails1, getUsedItemsDetails2))
                .isLast(false)
                .build();

        when(usedItemService.loadUsedItems(0))
                .thenReturn(getUsedItemsResponse);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/useditems")
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
                                fieldWithPath("loadUsedItemDetails[].transactionStatus").type(JsonFieldType.STRING)
                                        .description("중고상품 거래 상태 (ONGOING, RESERVE, COMPLETE)"),
                                fieldWithPath("loadUsedItemDetails[].imageName").type(JsonFieldType.STRING)
                                        .description("중고상품 이미지 (썸네일)"),
                                fieldWithPath("loadUsedItemDetails[].likeCount").type(JsonFieldType.NUMBER)
                                        .description("게시물 좋아요수"),
                                fieldWithPath("loadUsedItemDetails[].createAt").type(JsonFieldType.STRING)
                                        .description("중고상품 게시시간 (createAt과 현재시간과의 차이값을 프론트 화면에 렌더링)"),
                                fieldWithPath("loadUsedItemDetails[].itemStatus").type(JsonFieldType.STRING)
                                        .description("중고상품 상태"),
                                fieldWithPath("isLast").type(JsonFieldType.BOOLEAN)
                                        .description("페이지 마지막 유무 (false == 상품 더 있음 / true == 상품 없음")
                        )));
    }

    @Test
    @DisplayName("개별 중고상품 조회 (상세페이지)")
    void loadUsedItem() throws Exception {
        GetUsedItemResponse getUsedItemResponse = GetUsedItemResponse.builder()
                .title("중고상품이요")
                .category("윗도리")
                .content("강매는 아닌데 사십쇼.")
                .price(10000)
                .transactionType(TransactionType.DIRECT)
                .transactionMode(TransactionMode.BUY)
                .transactionStatus(TransactionStatus.ONGOING)
                .imageNames(List.of("https://domain/asdfasdfasdf"))
                .viewCount(0)
                .likeCount(0)
                .chattingCount(0)
                .marketMemberId(1L)
                .marketNickname("highyun")
                .isOwned(true)
                .createAt(LocalDateTime.now())
                .itemStatus(ItemStatus.ACTIVE)
                .build();

        when(usedItemService.loadUsedItem(any(), any()))
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
}
