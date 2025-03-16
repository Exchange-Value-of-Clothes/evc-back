package com.yzgeneration.evc.chat.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yzgeneration.evc.common.dto.SliceResponse;
import com.yzgeneration.evc.domain.chat.controller.ChatController;
import com.yzgeneration.evc.domain.chat.dto.ChatMessageResponse;
import com.yzgeneration.evc.domain.chat.dto.ChatMessageSliceResponse;
import com.yzgeneration.evc.domain.chat.dto.ChatRoomListResponse;
import com.yzgeneration.evc.domain.chat.service.ChatService;
import com.yzgeneration.evc.fixture.ChatFixture;
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
import java.util.ArrayList;
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

    @Test
    @WithFakeUser
    @DisplayName("거래하기 기능을 통해 채팅방을 생성하거나 조회한다.")
    void enter() throws Exception {
        List<ChatMessageResponse> response = new ArrayList<>();
        response.add(new ChatMessageResponse(1L, "message", LocalDateTime.MIN));
        ChatMessageSliceResponse chatMessageSliceResponse = new ChatMessageSliceResponse(1L, new SliceImpl<>(response, PageRequest.of(0, 10), false), LocalDateTime.MIN);
        given(chatService.getChatRoomByTradeRequest(any(), any(), any()))
                .willReturn(chatMessageSliceResponse);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/chat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ChatFixture.chatEnter()))
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.chatRoomId").value("1"))
                .andExpect(jsonPath("$.content[0].message").value("message"))
                .andExpect(jsonPath("$.content[0].senderId").value(1L))
                .andExpect(jsonPath("$.content[0].createdAt").value("+1000000000-01-01T00:00:00"))
                .andExpect(jsonPath("$.hasNext").value(false))
                .andExpect(jsonPath("$.size").value(10))
                .andExpect(jsonPath("$.numberOfElements").value(1))
                .andExpect(jsonPath("$.cursor").value("+1000000000-01-01T00:00:00"));
    }

    @Test
    @WithFakeUser
    @DisplayName("채팅목록을 조회한다.")
    void getChatRooms() throws Exception {
        SliceResponse<ChatRoomListResponse> response = new SliceResponse<>(
                new SliceImpl<>(List.of(new ChatRoomListResponse(1L,  "lastMessage", LocalDateTime.MIN)), PageRequest.of(0, 5), true),
                LocalDateTime.MIN
        );
        given(chatService.getChatRooms(any(), any()))
                .willReturn(response);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/chat")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].chatRoomId").value("1"))
                .andExpect(jsonPath("$.content[0].lastMessage").value("lastMessage"))
                .andExpect(jsonPath("$.content[0].createdAt").value("+1000000000-01-01T00:00:00"))
                .andExpect(jsonPath("$.hasNext").value(true))
                .andExpect(jsonPath("$.size").value(5))
                .andExpect(jsonPath("$.numberOfElements").value(1))
                .andExpect(jsonPath("$.cursor").value("+1000000000-01-01T00:00:00"));
    }

    @Test
    @WithFakeUser
    @DisplayName("채팅방을 조회한다.")
    void getChatRoom() throws Exception {
        List<ChatMessageResponse> response = new ArrayList<>();
        response.add(new ChatMessageResponse(1L, "message", LocalDateTime.MIN));
        ChatMessageSliceResponse chatMessageSliceResponse = new ChatMessageSliceResponse(1L, new SliceImpl<ChatMessageResponse>(response, PageRequest.of(0, 10), false), LocalDateTime.MIN);
        given(chatService.getChatRoomByListSelection(any(), any()))
                .willReturn(chatMessageSliceResponse);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/chat/{chatRoomId}", "1")
                        .param("cursor", LocalDateTime.MIN.toString())
                .with(csrf()))
                .andExpect(jsonPath("$.chatRoomId").value("1"))
                .andExpect(jsonPath("$.content[0].message").value("message"))
                .andExpect(jsonPath("$.content[0].senderId").value(1L))
                .andExpect(jsonPath("$.content[0].createdAt").value("+1000000000-01-01T00:00:00"))
                .andExpect(jsonPath("$.hasNext").value(false))
                .andExpect(jsonPath("$.size").value(10))
                .andExpect(jsonPath("$.numberOfElements").value(1))
                .andExpect(jsonPath("$.cursor").value("+1000000000-01-01T00:00:00"));

    }
}
