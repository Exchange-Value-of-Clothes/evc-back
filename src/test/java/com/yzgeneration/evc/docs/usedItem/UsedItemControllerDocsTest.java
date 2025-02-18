package com.yzgeneration.evc.docs.usedItem;

import com.yzgeneration.evc.docs.RestDocsSupport;
import com.yzgeneration.evc.domain.useditem.controller.UsedItemController;
import com.yzgeneration.evc.domain.useditem.dto.UsedItemRequest.CreateUsedItem;
import com.yzgeneration.evc.domain.useditem.dto.UsedItemResponse;
import com.yzgeneration.evc.domain.useditem.enums.UsedItemStatus;
import com.yzgeneration.evc.domain.useditem.model.ItemDetails;
import com.yzgeneration.evc.domain.useditem.model.ItemStats;
import com.yzgeneration.evc.domain.useditem.model.UsedItemTransaction;
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
import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
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
        CreateUsedItem usedItem = UsedItemFixture.fixCreateUsedItem(); // 실제 값 넣기
        MockMultipartFile usedItemReq = new MockMultipartFile("createUsedItem", "", "application/json", objectMapper.writeValueAsBytes(usedItem));

        UsedItemResponse usedItemResponse = UsedItemResponse.builder()
                .memberId(usedItem.getMemberId())
                .itemDetails(ItemDetails.create(usedItem.getCreateItemDetails()))
                .usedItemTransaction(UsedItemTransaction.create(usedItem.getCreateTransaction()))
                .usedItemStatus(UsedItemStatus.ACTIVE)
                .itemStats(ItemStats.create())
                .imageURLs(new ArrayList<>())
                .createAt(LocalDateTime.now())
                .build();

        when(usedItemService.createUsedItem(any(CreateUsedItem.class), anyList()))
                .thenReturn(usedItemResponse);


        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/useditems")
                        .file(usedItemReq)
                        .file("imageFiles", new MockUsedItemImageFile().getImageFiles().get(0).getBytes())
                        .file("imageFiles", new MockUsedItemImageFile().getImageFiles().get(1).getBytes())
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().isOk())
                .andDo(document("usedItem-create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestPartFields("createUsedItem",
                                fieldWithPath("memberId").type(JsonFieldType.NUMBER)
                                        .description("회원의 memberId"),
                                fieldWithPath("title").type(JsonFieldType.STRING)
                                        .description("중고상품 제목"),
                                fieldWithPath("category").type(JsonFieldType.STRING)
                                        .description("중고상품 카테고리"),
                                fieldWithPath("content").type(JsonFieldType.STRING)
                                        .description("중고상품 내용"),
                                fieldWithPath("price").type(JsonFieldType.NUMBER)
                                        .description("중고상품 가격"),
                                fieldWithPath("transactionType").type(JsonFieldType.STRING)
                                        .description("중고상품 거래유형"),
                                fieldWithPath("transactionMode").type(JsonFieldType.STRING)
                                        .description("중고상품 거래방법")
                        ),
                        responseFields(
                                fieldWithPath("memberId").type(JsonFieldType.NUMBER)
                                        .description("회원의 memberId"),
                                fieldWithPath("title").type(JsonFieldType.STRING)
                                        .description("중고상품 제목"),
                                fieldWithPath("category").type(JsonFieldType.STRING)
                                        .description("중고상품 카테고리"),
                                fieldWithPath("content").type(JsonFieldType.STRING)
                                        .description("중고상품 내용"),
                                fieldWithPath("price").type(JsonFieldType.NUMBER)
                                        .description("중고상품 가격"),
                                fieldWithPath("transactionType").type(JsonFieldType.STRING)
                                        .description("중고상품 거래유형"),
                                fieldWithPath("transactionMode").type(JsonFieldType.STRING)
                                        .description("중고상품 거래방법"),
                                fieldWithPath("transactionStatue").type(JsonFieldType.STRING)
                                        .description("중고상품 거래상태"),
                                fieldWithPath("usedItemStatus").type(JsonFieldType.STRING)
                                        .description("중고상품 게시상태"),
                                fieldWithPath("viewCount").type(JsonFieldType.NUMBER)
                                        .description("게시물 조회수"),
                                fieldWithPath("likeCount").type(JsonFieldType.NUMBER)
                                        .description("게시물 좋아요수"),
                                fieldWithPath("chattingCount").type(JsonFieldType.NUMBER)
                                        .description("게시물 채팅수"),
                                fieldWithPath("imageURLs").type(JsonFieldType.ARRAY)
                                        .description("중고상품 이미지 리스트"),
                                fieldWithPath("createAt").type(JsonFieldType.ARRAY)
                                        .description("중고상품 게시시간")
                        )));

    }
}
