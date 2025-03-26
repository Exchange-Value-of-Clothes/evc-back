package com.yzgeneration.evc.auctionitem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yzgeneration.evc.common.dto.SliceResponse;
import com.yzgeneration.evc.domain.item.auctionitem.controller.AuctionItemController;
import com.yzgeneration.evc.domain.item.auctionitem.dto.AuctionItemListResponse.AuctionItemPriceDetailsResponse;
import com.yzgeneration.evc.domain.item.auctionitem.dto.AuctionItemListResponse.GetAuctionItemListResponse;
import com.yzgeneration.evc.domain.item.auctionitem.dto.AuctionItemRequest.CreateAuctionItemRequest;
import com.yzgeneration.evc.domain.item.auctionitem.dto.AuctionItemResponse.AuctionItemDetailsResponse;
import com.yzgeneration.evc.domain.item.auctionitem.dto.AuctionItemResponse.AuctionItemStatsResponse;
import com.yzgeneration.evc.domain.item.auctionitem.dto.AuctionItemResponse.GetAuctionItemResponse;
import com.yzgeneration.evc.domain.item.auctionitem.service.AuctionItemService;
import com.yzgeneration.evc.domain.item.enums.TransactionType;
import com.yzgeneration.evc.domain.item.useditem.enums.ItemStatus;
import com.yzgeneration.evc.mock.WithFakeUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.SliceImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.filter.OncePerRequestFilter;

import java.time.LocalDateTime;
import java.util.List;

import static com.yzgeneration.evc.fixture.AuctionItemFixture.fixCreateAuctionItemRequest;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AuctionItemController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = OncePerRequestFilter.class),
        })
public class AuctionItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AuctionItemService auctionItemService;

    @Test
    @WithFakeUser
    @DisplayName("경매상품을 생성한다.")
    void createAuctionItem() throws Exception {

        CreateAuctionItemRequest createAuctionItemRequest = fixCreateAuctionItemRequest();
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auctionitems")
                        .content(objectMapper.writeValueAsString(createAuctionItemRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    @WithFakeUser
    @DisplayName("경매상품들을 조회한다. (Slice)")
    void getAuctionItems() throws Exception {

        AuctionItemPriceDetailsResponse auctionItemPriceDetailsResponse = new AuctionItemPriceDetailsResponse(5000, 5000, 1000);
        GetAuctionItemListResponse getAuctionItemListResponse = new GetAuctionItemListResponse(1L, "title", auctionItemPriceDetailsResponse, "imageNamge.jpg", LocalDateTime.MIN, LocalDateTime.MIN.plusDays(1), 1000);
        SliceResponse<GetAuctionItemListResponse> getAuctionItemSliceResponse = new SliceResponse<>(new SliceImpl<>(List.of(getAuctionItemListResponse), PageRequest.of(0, 10), true), LocalDateTime.MIN);

        when(auctionItemService.getAuctionItems(any(), any()))
                .thenReturn(getAuctionItemSliceResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/auctionitems")
                        .queryParam("cursor", LocalDateTime.MIN.toString())
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].auctionItemId").value(1L))
                .andExpect(jsonPath("$.content[0].title").value("title"))
                .andExpect(jsonPath("$.content[0].startPrice").value(5000))
                .andExpect(jsonPath("$.content[0].currentPrice").value(5000))
                .andExpect(jsonPath("$.content[0].bidPrice").value(1000))
                .andExpect(jsonPath("$.content[0].imageName").value("imageNamge.jpg"))
                .andExpect(jsonPath("$.content[0].startTime").value("+1000000000-01-01T00:00:00"))
                .andExpect(jsonPath("$.content[0].endTime").value("+1000000000-01-02T00:00:00"))
                .andExpect(jsonPath("$.content[0].point").value(1000))
                .andExpect(jsonPath("$.hasNext").value(true))
                .andExpect(jsonPath("$.size").value(10))
                .andExpect(jsonPath("$.numberOfElements").value(1))
                .andExpect(jsonPath("$.cursor").value("+1000000000-01-01T00:00:00"));
        ;
    }

    @Test
    @WithFakeUser
    @DisplayName("경매상품을 조회한다.")
    void getAuctionItem() throws Exception {

        AuctionItemDetailsResponse auctionItemDetailsResponse = new AuctionItemDetailsResponse("title", "category", "content");
        AuctionItemStatsResponse auctionItemStatsResponse = new AuctionItemStatsResponse(1, 1, 1);
        List<String> imageNameList = List.of("imageName.jpg");
        GetAuctionItemResponse getAuctionItemResponse = new GetAuctionItemResponse(auctionItemDetailsResponse, auctionItemStatsResponse, imageNameList, TransactionType.DIRECT, LocalDateTime.MIN, LocalDateTime.MIN.plusDays(1), 5000, 1L, "marketNickname", false, ItemStatus.ACTIVE);

        when(auctionItemService.getAuctionItem(any(), any()))
                .thenReturn(getAuctionItemResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/auctionitems/{auctionItemId}", 1L)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("title"))
                .andExpect(jsonPath("$.category").value("category"))
                .andExpect(jsonPath("$.content").value("content"))
                .andExpect(jsonPath("$.viewCount").value(1))
                .andExpect(jsonPath("$.likeCount").value(1))
                .andExpect(jsonPath("$.participantCount").value(1))
                .andExpect(jsonPath("$.imageNameList[0]").value("imageName.jpg"))
                .andExpect(jsonPath("$.transactionType").value("DIRECT"))
                .andExpect(jsonPath("$.startTime").value("+1000000000-01-01T00:00:00"))
                .andExpect(jsonPath("$.endTime").value("+1000000000-01-02T00:00:00"))
                .andExpect(jsonPath("$.currentPrice").value(5000))
                .andExpect(jsonPath("$.marketMemberId").value(1L))
                .andExpect(jsonPath("$.marketNickname").value("marketNickname"))
                .andExpect(jsonPath("$.isOwned").value(false))
                .andExpect(jsonPath("$.itemStatus").value("ACTIVE"));
    }
}
