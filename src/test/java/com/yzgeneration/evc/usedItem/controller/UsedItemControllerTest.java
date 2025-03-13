package com.yzgeneration.evc.usedItem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yzgeneration.evc.domain.useditem.controller.UsedItemController;
import com.yzgeneration.evc.domain.useditem.dto.UsedItemRequest.CreateUsedItemRequest;
import com.yzgeneration.evc.domain.useditem.dto.UsedItemResponse.LoadUsedItemResponse;
import com.yzgeneration.evc.domain.useditem.dto.UsedItemResponse.LoadUsedItemsDetails;
import com.yzgeneration.evc.domain.useditem.dto.UsedItemResponse.LoadUsedItemsResponse;
import com.yzgeneration.evc.domain.useditem.enums.TransactionMode;
import com.yzgeneration.evc.domain.useditem.enums.TransactionStatue;
import com.yzgeneration.evc.domain.useditem.enums.TransactionType;
import com.yzgeneration.evc.domain.useditem.enums.UsedItemStatus;
import com.yzgeneration.evc.domain.useditem.service.UsedItemService;
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

import static com.yzgeneration.evc.fixture.usedItem.UsedItemFixture.fixCreateUsedItemRequest;
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

        CreateUsedItemRequest usedItemRequest = fixCreateUsedItemRequest();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        MemberPrincipal memberPrincipal = (MemberPrincipal) authentication.getPrincipal();

        usedItemService.createUsedItem(memberPrincipal.getId(), usedItemRequest);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/useditems")
                        .content(objectMapper.writeValueAsString(usedItemRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    @WithFakeUser
    @DisplayName("중고상품 리스트 조회")
    void getUsedItems() throws Exception {

        LoadUsedItemsDetails loadUsedItemsDetails = LoadUsedItemsDetails.builder()
                .usedItemId(1L)
                .title("Test UsedItem")
                .price(10000)
                .transactionMode(TransactionMode.BUY)
                .transactionStatue(TransactionStatue.ONGOING)
                .imageURLs(List.of("http://localhost:8080/test-image/usedItem.jpg"))
                .likeCount(0)
                .createAt(LocalDateTime.now())
                .usedItemStatus(UsedItemStatus.ACTIVE)
                .build();

        LoadUsedItemsResponse loadUsedItemsResponse = LoadUsedItemsResponse.builder()
                .loadUsedItemDetails(List.of(loadUsedItemsDetails))
                .isLast(true)
                .build();

        when(usedItemService.loadUsedItems(anyInt()))
                .thenReturn(loadUsedItemsResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/useditems").param("page", "0"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(loadUsedItemsResponse)));
    }

    @Test
    @WithFakeUser
    @DisplayName("개별 중고상품 조회")
    void getUsedItem() throws Exception {

        LoadUsedItemResponse loadUsedItemResponse = LoadUsedItemResponse.builder()
                .title("Test UsedItem")
                .category("top shirt")
                .content("buy right now")
                .price(10000)
                .transactionType(TransactionType.DIRECT)
                .transactionMode(TransactionMode.BUY)
                .transactionStatue(TransactionStatue.ONGOING)
                .imageURLs(List.of("http://localhost:8080/test-image/usedItem.jpg"))
                .viewCount(0)
                .likeCount(0)
                .chattingCount(0)
                .marketMemberId(1L)
                .marketNickName("highyun")
                .isOwned(true)
                .createAt(LocalDateTime.now())
                .usedItemStatus(UsedItemStatus.ACTIVE)
                .build();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        MemberPrincipal memberPrincipal = (MemberPrincipal) authentication.getPrincipal();

        when(usedItemService.loadUsedItem(anyLong(), anyLong()))
                .thenReturn(loadUsedItemResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/useditems/{usedItemId}", memberPrincipal.getId()))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(loadUsedItemResponse)));
    }
}
