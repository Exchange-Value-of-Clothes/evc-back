package com.yzgeneration.evc.docs.auctionitem;

import com.yzgeneration.evc.docs.RestDocsSupport;
import com.yzgeneration.evc.domain.item.auctionitem.controller.AuctionBidController;
import com.yzgeneration.evc.domain.item.auctionitem.dto.AuctionRoomResponse;
import com.yzgeneration.evc.domain.item.auctionitem.service.AuctionBidService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuctionBidControllerDocsTest extends RestDocsSupport {
    private final AuctionBidService auctionBidService = mock(AuctionBidService.class);

    @Override
    protected Object initController() {
        return new AuctionBidController(auctionBidService);
    }

    @Test
    @DisplayName("경매방 등록 또는 조회")
    void createAuctionRoom() throws Exception {
        AuctionRoomResponse auctionRoomResponse = new AuctionRoomResponse(1L);
        when(auctionBidService.createOrGetAuctionRoom(any()))
                .thenReturn(auctionRoomResponse);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/bid/{auctionItemId}", 1L))
                .andExpect(status().isOk())
                .andDo(document("auctionBid-createRoom",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(parameterWithName("auctionItemId").description("경매상품의 id")),
                        responseFields(
                                fieldWithPath("auctionRoomId").type(JsonFieldType.NUMBER)
                                        .description("경매방 Id (경매를 위한 소켓 연결시 필요)")
                        )
                ));

    }
}
