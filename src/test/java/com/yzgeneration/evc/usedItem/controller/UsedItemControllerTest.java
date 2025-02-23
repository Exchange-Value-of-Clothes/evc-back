package com.yzgeneration.evc.usedItem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yzgeneration.evc.domain.useditem.controller.UsedItemController;
import com.yzgeneration.evc.domain.useditem.dto.UsedItemRequest.CreateUsedItemRequest;
import com.yzgeneration.evc.domain.useditem.dto.UsedItemResponse.CreateUsedItemResponse;
import com.yzgeneration.evc.domain.useditem.service.UsedItemService;
import com.yzgeneration.evc.mock.usedItem.MockUsedItemImageFile;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.filter.OncePerRequestFilter;

import static com.yzgeneration.evc.fixture.usedItem.UsedItemFixture.fixCreateUsedItemRequest;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
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
    @WithMockUser
    @DisplayName("중고상품을 생성한다.")
    void createUsedItem() throws Exception {
        CreateUsedItemRequest usedItemRequest = fixCreateUsedItemRequest();

        MockMultipartFile usedItemReq = new MockMultipartFile("createUsedItemRequest", "", "application/json", objectMapper.writeValueAsBytes(usedItemRequest));

        CreateUsedItemResponse createUsedItemResponse = new CreateUsedItemResponse(usedItemRequest.getMemberId(), 1L);

        when(usedItemService.createUsedItem(any(CreateUsedItemRequest.class), anyList()))
                .thenReturn(createUsedItemResponse);

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/useditems")
                        .file(usedItemReq)
                        .file("imageFiles", new MockUsedItemImageFile().getImageFiles().get(0).getBytes())
                        .file("imageFiles", new MockUsedItemImageFile().getImageFiles().get(1).getBytes())
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(createUsedItemResponse)));
    }
}
