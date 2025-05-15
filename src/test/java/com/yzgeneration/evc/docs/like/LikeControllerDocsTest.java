package com.yzgeneration.evc.docs.like;

import com.yzgeneration.evc.common.dto.SliceResponse;
import com.yzgeneration.evc.docs.RestDocsSupport;
import com.yzgeneration.evc.domain.item.enums.ItemType;
import com.yzgeneration.evc.domain.item.enums.TransactionMode;
import com.yzgeneration.evc.domain.item.enums.TransactionStatus;
import com.yzgeneration.evc.domain.like.controller.LikeController;
import com.yzgeneration.evc.domain.like.dto.LikeItemsResponse;
import com.yzgeneration.evc.domain.like.dto.LikeResponse;
import com.yzgeneration.evc.domain.like.service.LikeService;
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
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class LikeControllerDocsTest extends RestDocsSupport {
    private final LikeService likeService = mock(LikeService.class);

    @Override
    protected Object initController() {
        return new LikeController(likeService);
    }

    @Test
    @DisplayName("상품에 좋아요 (토글 형태)")
    void like() throws Exception {
        LikeResponse likeResponse = new LikeResponse(true, 1L);

        when(likeService.toggleLike(any(), any(), any()))
                .thenReturn(likeResponse);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/likes/{itemId}", 1L)
                        .queryParam("itemType", ItemType.USEDITEM.name()))
                .andExpect(status().isOk())
                .andDo(document("like-toggle",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(parameterWithName("itemId").description("상품의 id")),
                        queryParameters(parameterWithName("itemType").description("상품의 type (USEDITEM, AUCTIONITEM)")),
                        responseFields(
                                fieldWithPath("isLike").type(JsonFieldType.BOOLEAN)
                                        .description("좋아요 여부 (true = 좋아요 / false = 좋아요 취소)"),
                                fieldWithPath("likeCount").type(JsonFieldType.NUMBER)
                                        .description("해당 상품의 좋아요 총 개수"))));
    }

    @Test
    @DisplayName("내 좋아요 상품 조회")
    void getMyLikedItems() throws Exception {
        LikeItemsResponse likeResponse = new LikeItemsResponse(0L, "title", 1000, TransactionMode.BUY, TransactionStatus.ONGOING, "imageName", 0L, LocalDateTime.MIN);
        SliceResponse<LikeItemsResponse> likeItemsResponseSliceResponse = new SliceResponse<>(new SliceImpl<>(List.of(likeResponse), PageRequest.of(0, 10), true), LocalDateTime.MIN);

        when(likeService.getMyLikedItems(any()))
                .thenReturn(likeItemsResponseSliceResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/likes/my"))
                .andExpect(status().isOk())
                .andDo(document("like-my-get",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("content").type(JsonFieldType.ARRAY)
                                        .description("상품 정보 리스트"),
                                fieldWithPath("content[].itemId").type(JsonFieldType.NUMBER)
                                        .description("상품 id"),
                                fieldWithPath("content[].title").type(JsonFieldType.STRING)
                                        .description("상품 제목"),
                                fieldWithPath("content[].price").type(JsonFieldType.NUMBER)
                                        .description("상품 가격"),
                                fieldWithPath("content[].transactionMode").type(JsonFieldType.STRING)
                                        .description("상품 거래방법 (SELL, BUY, AUCTION)"),
                                fieldWithPath("content[].transactionStatus").type(JsonFieldType.STRING)
                                        .description("상품 거래 상태 (ONGOING, RESERVE, COMPLETE)"),
                                fieldWithPath("content[].imageName").type(JsonFieldType.STRING)
                                        .description("상품 이미지 (썸네일)"),
                                fieldWithPath("content[].likeCount").type(JsonFieldType.NUMBER)
                                        .description("게시물 좋아요수"),
                                fieldWithPath("content[].createAt").type(JsonFieldType.STRING)
                                        .description("상품 게시시간 (createAt과 현재시간과의 차이값을 프론트 화면에 렌더링)"),
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
