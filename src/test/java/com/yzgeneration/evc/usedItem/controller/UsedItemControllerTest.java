package com.yzgeneration.evc.usedItem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import com.yzgeneration.evc.mock.WithFakeUser;
import com.yzgeneration.evc.security.MemberPrincipal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.filter.OncePerRequestFilter;

import java.time.LocalDateTime;
import java.util.List;

import static com.yzgeneration.evc.fixture.UsedItemFixture.fixCreateUsedItemRequest;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        MemberPrincipal memberPrincipal = (MemberPrincipal) authentication.getPrincipal();

        usedItemService.createUsedItem(memberPrincipal.getId(), createUsedItemRequest);

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

        GetUsedItemsDetails getUsedItemsDetails = GetUsedItemsDetails.builder()
                .usedItemId(1L)
                .title("title")
                .price(5000)
                .transactionMode(TransactionMode.BUY)
                .transactionStatus(TransactionStatus.ONGOING)
                .imageName("imageName.jpg")
                .likeCount(0)
                .createAt(LocalDateTime.now())
                .itemStatus(ItemStatus.ACTIVE)
                .build();

        GetUsedItemsResponse getUsedItemsResponse = GetUsedItemsResponse.builder()
                .loadUsedItemDetails(List.of(getUsedItemsDetails))
                .isLast(true)
                .build();

        when(usedItemService.loadUsedItems(anyInt()))
                .thenReturn(getUsedItemsResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/useditems").param("page", "0"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(getUsedItemsResponse)));
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
                .viewCount(0)
                .likeCount(0)
                .chattingCount(0)
                .marketMemberId(1L)
                .marketNickname("marketNickname")
                .isOwned(true)
                .createAt(LocalDateTime.now())
                .itemStatus(ItemStatus.ACTIVE)
                .build();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        MemberPrincipal memberPrincipal = (MemberPrincipal) authentication.getPrincipal();

        when(usedItemService.loadUsedItem(anyLong(), anyLong()))
                .thenReturn(getUsedItemResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/useditems/{usedItemId}", memberPrincipal.getId()))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(getUsedItemResponse)));
    }
}
