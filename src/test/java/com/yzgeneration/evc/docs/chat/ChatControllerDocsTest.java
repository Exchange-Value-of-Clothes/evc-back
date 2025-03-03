package com.yzgeneration.evc.docs.chat;

import com.yzgeneration.evc.common.dto.SliceResponse;
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

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.SliceImpl;
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
        SliceResponse<ChatRoomListResponse> response = new SliceResponse<>(
                new SliceImpl<>(List.of(new ChatRoomListResponse(1L, "hi", LocalDateTime.MIN)), PageRequest.of(0, 10), true),
                LocalDateTime.MIN
        );
        given(chatService.getChatRooms(any()))
                .willReturn(response);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/chat")
                        .with(csrf()))
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
