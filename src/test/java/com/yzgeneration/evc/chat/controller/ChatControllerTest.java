package com.yzgeneration.evc.chat.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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
        given(chatService.getChatRooms(any()))
                .willReturn(List.of(new ChatRoomListResponse(1L, 1L, "lastMessage", LocalDateTime.MIN)));
        mockMvc.perform(MockMvcRequestBuilders.get("/api/chat")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].chatRoomId").value("1"))
                .andExpect(jsonPath("$[0].usedItemId").value("1"))
                .andExpect(jsonPath("$[0].lastMessage").value("lastMessage"))
                .andExpect(jsonPath("$[0].createdAt").value("-999999999-01-01T00:00:00"));
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
