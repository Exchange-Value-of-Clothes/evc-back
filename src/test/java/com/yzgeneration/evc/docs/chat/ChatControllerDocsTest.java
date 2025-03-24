package com.yzgeneration.evc.docs.chat;

import com.yzgeneration.evc.common.dto.SliceResponse;
import com.yzgeneration.evc.docs.RestDocsSupport;
import com.yzgeneration.evc.domain.chat.controller.ChatController;
import com.yzgeneration.evc.domain.chat.dto.ChatMessageResponse;
import com.yzgeneration.evc.domain.chat.dto.ChatMessageSliceResponse;
import com.yzgeneration.evc.domain.chat.dto.ChatRoomListResponse;
import com.yzgeneration.evc.domain.chat.implement.ChatConnectionManager;
import com.yzgeneration.evc.domain.chat.service.ChatService;
import com.yzgeneration.evc.fixture.ChatFixture;

import com.yzgeneration.evc.mock.WithFakeUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.SliceImpl;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;

import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ChatControllerDocsTest extends RestDocsSupport {

    private final ChatService chatService = mock(ChatService.class);

    @Override
    protected Object initController() {
        return new ChatController(chatService);
    }

    @Test
    @DisplayName("거래하기 기능을 통해 채팅방을 생성하거나 조회한다.")
    void enter() throws Exception {
        List<ChatMessageResponse> response = new ArrayList<>();
        response.add(new ChatMessageResponse(1L, true, "message", LocalDateTime.MIN));
        ChatMessageSliceResponse chatMessageSliceResponse = new ChatMessageSliceResponse(1L, new SliceImpl<>(response, PageRequest.of(0, 10), false), LocalDateTime.MIN);
        given(chatService.getChatRoomByTradeRequest(any(), any(), any()))
                .willReturn(chatMessageSliceResponse);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/chat")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(ChatFixture.chatEnter())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.chatRoomId").value("1"))
                .andExpect(jsonPath("$.content[0].message").value("message"))
                .andExpect(jsonPath("$.content[0].senderId").value(1L))
                .andExpect(jsonPath("$.content[0].isMine").value(true))
                .andExpect(jsonPath("$.content[0].createdAt").value("+1000000000-01-01T00:00:00"))
                .andExpect(jsonPath("$.hasNext").value(false))
                .andExpect(jsonPath("$.size").value(10))
                .andExpect(jsonPath("$.numberOfElements").value(1))
                .andExpect(jsonPath("$.cursor").value("+1000000000-01-01T00:00:00"))
                .andDo(document("chat-enter",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("usedItemId").type(JsonFieldType.NUMBER)
                                        .description("중고상품 아이디"),
                                fieldWithPath("ownerId").type(JsonFieldType.NUMBER)
                                        .description("중고상품 소유자 아이디")
                        ),
                        responseFields(
                                fieldWithPath("chatRoomId").type(JsonFieldType.NUMBER)
                                        .description("채팅방 아이디"),
                                fieldWithPath("content").type(JsonFieldType.ARRAY)
                                        .description("채팅 정보 리스트"),
                                fieldWithPath("content[].message").type(JsonFieldType.STRING)
                                        .description("채팅 메시지"),
                                fieldWithPath("content[].senderId").type(JsonFieldType.NUMBER)
                                        .description("채팅 보낸 멤버의 id"),
                                fieldWithPath("content[].isMine").type(JsonFieldType.BOOLEAN)
                                        .description("본인 소유 채팅 여부"),
                                fieldWithPath("content[].createdAt").type(JsonFieldType.STRING)
                                        .description("메시지 전송 시간 (예: yyyy-MM-dd'T'HH:mm:ss)"),
                                fieldWithPath("hasNext").type(JsonFieldType.BOOLEAN)
                                        .description("다음 페이지 존재여부"),
                                fieldWithPath("size").type(JsonFieldType.NUMBER)
                                        .description("페이지 요청 사이즈"),
                                fieldWithPath("numberOfElements").type(JsonFieldType.NUMBER)
                                        .description("조회된 데이터 개수"),
                                fieldWithPath("cursor").type(JsonFieldType.STRING)
                                        .description("다음 페이지 커서")

                        )));
    }

    @Test
    @DisplayName("채팅목록을 조회한다.")
    void getChatRooms() throws Exception {
        SliceResponse<ChatRoomListResponse> response = new SliceResponse<>(
                new SliceImpl<>(List.of(new ChatRoomListResponse(1L, "hi", LocalDateTime.MIN)), PageRequest.of(0, 10), true),
                LocalDateTime.MIN
        );
        given(chatService.getChatRooms(any(), any()))
                .willReturn(response);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/chat"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].chatRoomId").value(1))
                .andExpect(jsonPath("$.content[0].lastMessage").value("hi"))
                .andExpect(jsonPath("$.content[0].createdAt").value("+1000000000-01-01T00:00:00"))
                .andDo(document("chat-getChatRooms",
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("content[].chatRoomId").type(JsonFieldType.NUMBER)
                                        .description("채팅방 아이디"),
                                fieldWithPath("content[].usedItemId").type(JsonFieldType.NULL)
                                        .optional()
                                        .description("중고상품 아이디 (없을 수도 있음)"),
                                fieldWithPath("content[].lastMessage").type(JsonFieldType.STRING)
                                        .description("최근 메시지"),
                                fieldWithPath("content[].createdAt").type(JsonFieldType.STRING)
                                        .description("최근 메시지 전송 시간 (예: yyyy-MM-dd'T'HH:mm:ss)"),
                                fieldWithPath("hasNext").type(JsonFieldType.BOOLEAN)
                                        .description("다음 페이지 존재 여부"),
                                fieldWithPath("size").type(JsonFieldType.NUMBER)
                                        .description("한 페이지당 요소 개수"),
                                fieldWithPath("numberOfElements").type(JsonFieldType.NUMBER)
                                        .description("현재 페이지의 요소 개수"),
                                fieldWithPath("cursor").type(JsonFieldType.STRING)
                                        .description("다음 페이지를 위한 커서 값")
                        )));
    }

    @Test
    @DisplayName("채팅방을 조회한다.")
    void getChatRoom() throws Exception {
        List<ChatMessageResponse> response = new ArrayList<>();
        response.add(new ChatMessageResponse(1L, true, "message", LocalDateTime.MIN));
        ChatMessageSliceResponse chatMessageSliceResponse = new ChatMessageSliceResponse(1L, new SliceImpl<ChatMessageResponse>(response, PageRequest.of(0, 10), false), LocalDateTime.MIN);
        given(chatService.getChatRoomByListSelection(any(), any(), any()))
                .willReturn(chatMessageSliceResponse);

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/chat/{chatRoomId}", "1")
                        .param("cursor", LocalDateTime.MIN.toString())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.chatRoomId").value("1"))
                .andExpect(jsonPath("$.content[0].message").value("message"))
                .andExpect(jsonPath("$.content[0].senderId").value(1L))
                .andExpect(jsonPath("$.content[0].isMine").value(true))
                .andExpect(jsonPath("$.content[0].createdAt").value("+1000000000-01-01T00:00:00"))
                .andExpect(jsonPath("$.hasNext").value(false))
                .andExpect(jsonPath("$.size").value(10))
                .andExpect(jsonPath("$.numberOfElements").value(1))
                .andExpect(jsonPath("$.cursor").value("+1000000000-01-01T00:00:00"))
                .andDo(document("chat-getChatRoom",
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("chatRoomId").description("채팅방 아이디")
                        ),
                        queryParameters(
                                parameterWithName("cursor")
                                        .optional()
                                        .description("이전 메시지 조회를 위한 커서 (ISO-8601 형식: yyyy-MM-dd'T'HH:mm:ss)")
                        ),
                        responseFields(
                                fieldWithPath("chatRoomId").type(JsonFieldType.NUMBER)
                                        .description("채팅방 아이디"),
                                fieldWithPath("content").type(JsonFieldType.ARRAY)
                                        .description("채팅 정보 리스트"),
                                fieldWithPath("content[].message").type(JsonFieldType.STRING)
                                        .description("채팅 메시지"),
                                fieldWithPath("content[].senderId").type(JsonFieldType.NUMBER)
                                        .description("채팅 보낸 멤버의 id"),
                                fieldWithPath("content[].isMine").type(JsonFieldType.BOOLEAN)
                                        .description("채팅 보낸 멤버의 id"),
                                fieldWithPath("content[].createdAt").type(JsonFieldType.STRING)
                                        .description("메시지 전송 시간 (예: yyyy-MM-dd'T'HH:mm:ss)"),
                                fieldWithPath("hasNext").type(JsonFieldType.BOOLEAN)
                                        .description("다음 페이지 존재여부"),
                                fieldWithPath("size").type(JsonFieldType.NUMBER)
                                        .description("페이지 요청 사이즈"),
                                fieldWithPath("numberOfElements").type(JsonFieldType.NUMBER)
                                        .description("조회된 데이터 개수"),
                                fieldWithPath("cursor").type(JsonFieldType.STRING)
                                        .description("다음 페이지 커서")

                        )));
    }

    @Test
    @DisplayName("채팅방을 나간다.")
    void exitChatRoom() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch("/api/chat/{chatRoomId}/exit", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("chat-exitChatRoom",
                        pathParameters(
                                parameterWithName("chatRoomId").description("채팅방 아이디")
                        )
                ));
    }
}
