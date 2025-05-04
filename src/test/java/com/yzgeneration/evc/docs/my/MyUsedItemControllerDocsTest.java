package com.yzgeneration.evc.docs.my;

import com.yzgeneration.evc.common.dto.SliceResponse;
import com.yzgeneration.evc.docs.RestDocsSupport;
import com.yzgeneration.evc.domain.item.enums.TransactionMode;
import com.yzgeneration.evc.domain.item.enums.TransactionStatus;
import com.yzgeneration.evc.domain.item.useditem.dto.MyOrMemberUsedItemsResponse;
import com.yzgeneration.evc.domain.item.useditem.dto.UsedItemsResponse.GetMyOrMemberUsedItemsResponse;
import com.yzgeneration.evc.domain.item.useditem.enums.ItemStatus;
import com.yzgeneration.evc.domain.item.useditem.service.UsedItemService;
import com.yzgeneration.evc.domain.my.controller.MyUsedItemController;
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

public class MyUsedItemControllerDocsTest extends RestDocsSupport {
    private final UsedItemService usedItemService = mock(UsedItemService.class);

    @Override
    protected Object initController() {
        return new MyUsedItemController(usedItemService);
    }

    @Test
    @DisplayName("내 중고상품 전체 조회")
    void getMyUsedItems() throws Exception {
        GetMyOrMemberUsedItemsResponse getMyOrMemberUsedItemsResponse = new GetMyOrMemberUsedItemsResponse(1L, "title", 5000, TransactionMode.BUY, TransactionStatus.ONGOING, "imageName", 1L, LocalDateTime.MIN, ItemStatus.ACTIVE);
        SliceResponse<GetMyOrMemberUsedItemsResponse> getMyOrMemberUsedItems = new SliceResponse<>(new SliceImpl<>(List.of(getMyOrMemberUsedItemsResponse), PageRequest.of(0, 10), true), LocalDateTime.MIN);
        MyOrMemberUsedItemsResponse myOrMemberUsedItems = new MyOrMemberUsedItemsResponse(1L, getMyOrMemberUsedItems);

        when(usedItemService.getMyOrMemberUsedItems(any(), any(), any()))
                .thenReturn(myOrMemberUsedItems);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/my/useditems")
                        .param("cursor", LocalDateTime.MIN.toString())
                        .param("condition", TransactionMode.BUY.name()))
                .andExpect(status().isOk())
                .andDo(document("myUsedItems-get",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        queryParameters(
                                parameterWithName("cursor").description("이전 메시지 조회를 위한 커서 (ISO-8601 형식: yyyy-MM-dd'T'HH:mm:ss)"),
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
}
