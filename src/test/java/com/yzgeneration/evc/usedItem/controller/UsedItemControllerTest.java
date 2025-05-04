package com.yzgeneration.evc.usedItem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yzgeneration.evc.common.dto.SliceResponse;
import com.yzgeneration.evc.domain.item.enums.TransactionMode;
import com.yzgeneration.evc.domain.item.enums.TransactionStatus;
import com.yzgeneration.evc.domain.item.enums.TransactionType;
import com.yzgeneration.evc.domain.item.useditem.controller.UsedItemController;
import com.yzgeneration.evc.domain.item.useditem.dto.UsedItemListResponse.GetUsedItemListResponse;
import com.yzgeneration.evc.domain.item.useditem.dto.UsedItemRequest.CreateUsedItemRequest;
import com.yzgeneration.evc.domain.item.useditem.dto.UsedItemResponse.GetUsedItemResponse;
import com.yzgeneration.evc.domain.item.useditem.enums.ItemStatus;
import com.yzgeneration.evc.domain.item.useditem.service.UsedItemService;
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

import static com.yzgeneration.evc.fixture.UsedItemFixture.fixCreateUsedItemRequest;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UsedItemController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = OncePerRequestFilter.class),
        })
class UsedItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UsedItemService usedItemService;

    @Test
    @WithFakeUser
    @DisplayName("중고상품을 생성한다.")
    void createUsedItem() throws Exception {

        CreateUsedItemRequest createUsedItemRequest = fixCreateUsedItemRequest();
        mockMvc.perform(MockMvcRequestBuilders.post("/api/useditems")
                        .content(objectMapper.writeValueAsString(createUsedItemRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    @WithFakeUser
    @DisplayName("중고상품 리스트 조회")
    void getUsedItems() throws Exception {

        GetUsedItemListResponse getUsedItemListResponse = new GetUsedItemListResponse(1L, "title", 5000, TransactionMode.BUY, TransactionStatus.ONGOING, "imageName.jpg", 1L, LocalDateTime.MIN, ItemStatus.ACTIVE);
        SliceResponse<GetUsedItemListResponse> getUsedItemSliceResponse = new SliceResponse<>(new SliceImpl<>(List.of(getUsedItemListResponse), PageRequest.of(0, 10), true), LocalDateTime.MIN);

        when(usedItemService.getUsedItems(any()))
                .thenReturn(getUsedItemSliceResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/useditems")
                        .queryParam("cursor", LocalDateTime.MIN.toString())
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].usedItemId").value(1L))
                .andExpect(jsonPath("$.content[0].title").value("title"))
                .andExpect(jsonPath("$.content[0].price").value(5000))
                .andExpect(jsonPath("$.content[0].transactionMode").value("BUY"))
                .andExpect(jsonPath("$.content[0].transactionStatus").value("ONGOING"))
                .andExpect(jsonPath("$.content[0].imageName").value("imageName.jpg"))
                .andExpect(jsonPath("$.content[0].likeCount").value(1))
                .andExpect(jsonPath("$.content[0].createAt").value("+1000000000-01-01T00:00:00"))
                .andExpect(jsonPath("$.content[0].itemStatus").value("ACTIVE"))
                .andExpect(jsonPath("$.hasNext").value(true))
                .andExpect(jsonPath("$.size").value(10))
                .andExpect(jsonPath("$.numberOfElements").value(1))
                .andExpect(jsonPath("$.cursor").value("+1000000000-01-01T00:00:00"));
    }

    @Test
    @WithFakeUser
    @DisplayName("개별 중고상품 조회")
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

        mockMvc.perform(MockMvcRequestBuilders.get("/api/useditems/{usedItemId}", 1L)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("title"))
                .andExpect(jsonPath("$.category").value("category"))
                .andExpect(jsonPath("$.content").value("content"))
                .andExpect(jsonPath("$.price").value(5000))
                .andExpect(jsonPath("$.transactionType").value("DIRECT"))
                .andExpect(jsonPath("$.transactionMode").value("BUY"))
                .andExpect(jsonPath("$.transactionStatus").value("ONGOING"))
                .andExpect(jsonPath("$.imageNames[0]").value("imageName.jpg"))
                .andExpect(jsonPath("$.viewCount").value(1L))
                .andExpect(jsonPath("$.likeCount").value(1L))
                .andExpect(jsonPath("$.chattingCount").value(1L))
                .andExpect(jsonPath("$.marketMemberId").value(1L))
                .andExpect(jsonPath("$.marketNickname").value("marketNickname"))
                .andExpect(jsonPath("$.isOwned").value(true))
                .andExpect(jsonPath("$.createAt").value("+1000000000-01-01T00:00:00"))
                .andExpect(jsonPath("$.itemStatus").value("ACTIVE"));

    }
}
