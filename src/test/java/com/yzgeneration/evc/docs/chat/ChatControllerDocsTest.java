package com.yzgeneration.evc.docs.chat;

import com.yzgeneration.evc.docs.RestDocsSupport;
import com.yzgeneration.evc.domain.chat.controller.ChatController;
import com.yzgeneration.evc.domain.chat.dto.ChatRoomListResponse;
import com.yzgeneration.evc.domain.chat.implement.ChatConnectionManager;
import com.yzgeneration.evc.domain.chat.service.ChatService;
import com.yzgeneration.evc.domain.member.model.Member;
import com.yzgeneration.evc.fixture.MemberFixture;
import com.yzgeneration.evc.mock.WithFakeUser;
import com.yzgeneration.evc.security.MemberPrincipal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ChatControllerDocsTest extends RestDocsSupport {

    private final ChatService chatService = mock(ChatService.class);
    private final ChatConnectionManager chatConnectionManager = mock(ChatConnectionManager.class);

    @Override
    protected Object initController() {
        return new ChatController(chatService, chatConnectionManager);
    }

    @Test
    @DisplayName("채팅방을 생성한다.")
    void createChatRoom() throws Exception {
        MemberPrincipal memberPrincipal = new MemberPrincipal(MemberFixture.withFakeUser());
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                memberPrincipal,
                memberPrincipal.getMember().getId(),
                memberPrincipal.getAuthorities());
        mockMvc.perform(MockMvcRequestBuilders.post("/api/chat")
                        .queryParam("usedItemId", "1")
                        .with(csrf())
                        .with(SecurityMockMvcRequestPostProcessors.authentication(authentication))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andDo(document("chat-create",
                        preprocessResponse(prettyPrint()),
                        queryParameters(parameterWithName("usedItemId").description("중고상품 아이디")),
                        responseFields(
                                fieldWithPath("success").type(JsonFieldType.BOOLEAN)
                                        .description("성공여부")
                        )));
    }

    @Test
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
                .andExpect(jsonPath("$[0].createdAt").value("+1000000000-01-01T00:00:00"))
                .andDo(document("chat-getChatRooms",
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].chatRoomId").type(JsonFieldType.NUMBER)
                                        .description("채팅방 아이디"),
                                fieldWithPath("[].usedItemId").type(JsonFieldType.NUMBER)
                                        .description("중고상품 아이디"),
                                fieldWithPath("[].lastMessage").type(JsonFieldType.STRING)
                                        .description("최근 메시지"),
                                fieldWithPath("[].createdAt").type(JsonFieldType.STRING)
                                        .description("최근 메시지 전송 시간 (예: yyyy-MM-dd'T'HH:mm:ss)")
                        )));
    }

    @Test
    @WithFakeUser
    @DisplayName("채팅방에 입장한다.")
    void enterChatRoom() throws Exception {
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/chat/{chatRoomId}", "1")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andDo(document("chat-enter",
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("chatRoomId").description("채팅방 아이디")
                        ),
                        responseFields(
                                 fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공여부")
                        )));
    }
}
