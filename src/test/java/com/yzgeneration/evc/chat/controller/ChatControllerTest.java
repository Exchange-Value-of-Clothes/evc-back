package com.yzgeneration.evc.chat.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yzgeneration.evc.common.dto.SliceResponse;
import com.yzgeneration.evc.domain.chat.controller.ChatController;
import com.yzgeneration.evc.domain.chat.dto.ChatRoomListResponse;
import com.yzgeneration.evc.domain.chat.implement.ChatConnectionManager;
import com.yzgeneration.evc.domain.chat.service.ChatService;
import com.yzgeneration.evc.mock.WithFakeUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.SliceImpl;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.filter.OncePerRequestFilter;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ChatController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = OncePerRequestFilter.class),
        })
class ChatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ChatService chatService;

    @MockitoBean
    private ChatConnectionManager chatConnectionManager;

    @Test
    @WithFakeUser
    @DisplayName("채팅방을 생성한다.")
    void createChatRoom() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/chat")
                .queryParam("usedItemId", "1")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"));
    }

    @Test
    @WithFakeUser
    @DisplayName("채팅방을 조회한다.")
    void getChatRooms() throws Exception {
        SliceResponse<ChatRoomListResponse> response = new SliceResponse<>(
                new SliceImpl<>(List.of(new ChatRoomListResponse(1L,  "lastMessage", LocalDateTime.MIN)), PageRequest.of(0, 10), true),
                LocalDateTime.MIN
        );
        given(chatService.getChatRooms(any(), any()))
                .willReturn(response);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/chat")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].chatRoomId").value("1"))
                .andExpect(jsonPath("$.content[0].lastMessage").value("lastMessage"))
                .andExpect(jsonPath("$.content[0].createdAt").value("+1000000000-01-01T00:00:00"));
    }

    @Test
    @WithFakeUser
    @DisplayName("채팅방에 입장한다.")
    void enterChatRoom() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/chat/{chatRoomId}", "1")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }
}
