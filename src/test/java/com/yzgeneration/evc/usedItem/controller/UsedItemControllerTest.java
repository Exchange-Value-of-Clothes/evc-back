package com.yzgeneration.evc.usedItem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yzgeneration.evc.domain.useditem.controller.UsedItemController;
import com.yzgeneration.evc.domain.useditem.service.UsedItemService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.filter.OncePerRequestFilter;

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
    @WithMockUser
    @DisplayName("중고상품을 생성한다.")
    void createUsedItem() throws Exception {
//        CreateUsedItem usedItem = fixCreateUsedItem();
//
//        Member memberMock = mock(Member.class);
//        when(memberMock.getId()).thenReturn(1L);
//
//        MemberPrincipal memberPrincipal = mock(MemberPrincipal.class);
//        when(memberPrincipal.getMember()).thenReturn(memberMock);
//
//        MockMultipartFile usedItemReq = new MockMultipartFile("createUsedItem", "", "application/json", objectMapper.writeValueAsBytes(usedItem));
//
//        UsedItemResponse usedItemResponse = UsedItemResponse.builder()
//                .itemDetails(ItemDetails.create(usedItem.getCreateItemDetails()))
//                .usedItemTransaction(UsedItemTransaction.create(usedItem.getCreateTransaction()))
//                .usedItemStatus(UsedItemStatus.ACTIVE)
//                .itemStats(ItemStats.create())
//                .imageURLs(new ArrayList<>())
//                .createAt(LocalDateTime.now())
//                .build();
//
//        when(usedItemService.createUsedItem(any(Long.class), any(CreateUsedItem.class), anyList()))
//                .thenReturn(usedItemResponse);
//        when(memberPrincipal.getMember().getId()).thenReturn(1L);
//
//        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/useditems")
//                        .file(usedItemReq)
//                        .file("imageFiles", new MockUsedItemImageFile().getImageFiles().get(0).getBytes())
//                        .file("imageFiles", new MockUsedItemImageFile().getImageFiles().get(1).getBytes())
//                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
//                        .with(csrf()))
//                .andExpect(status().isOk())
//                .andExpect(content().json(objectMapper.writeValueAsString(usedItemResponse)));
    }
}
