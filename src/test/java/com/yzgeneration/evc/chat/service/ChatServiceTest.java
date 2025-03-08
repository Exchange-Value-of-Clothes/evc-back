package com.yzgeneration.evc.chat.service;

import com.yzgeneration.evc.common.dto.SliceResponse;
import com.yzgeneration.evc.domain.chat.dto.ChatMessageSliceResponse;
import com.yzgeneration.evc.domain.chat.dto.ChatRoomListResponse;
import com.yzgeneration.evc.domain.chat.implement.ChatConnectionManager;
import com.yzgeneration.evc.domain.chat.implement.SessionAttributeAccessor;
import com.yzgeneration.evc.domain.chat.infrastructure.ChatConnectionRepository;
import com.yzgeneration.evc.domain.chat.infrastructure.ChatMessageRepository;
import com.yzgeneration.evc.domain.chat.model.ChatMessage;
import com.yzgeneration.evc.domain.chat.service.ChatService;
import com.yzgeneration.evc.mock.chat.FakeChatConnectionRepository;
import com.yzgeneration.evc.mock.chat.FakeChatMemberRepository;
import com.yzgeneration.evc.mock.chat.FakeChatMessageRepository;
import com.yzgeneration.evc.mock.chat.FakeChatRoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class ChatServiceTest {

    private ChatService chatService;
    private ChatConnectionRepository connectionRepository;
    private ChatConnectionManager chatConnectionManager;
    private ChatMessageRepository chatMessageRepository;

    @BeforeEach
    void init() {
        connectionRepository = new FakeChatConnectionRepository();
        chatConnectionManager = new ChatConnectionManager(connectionRepository);
        chatMessageRepository = new FakeChatMessageRepository();
        chatService = new ChatService(
                new FakeChatRoomRepository(),
                new FakeChatMemberRepository(),
                chatMessageRepository,
                chatConnectionManager,
                new SessionAttributeAccessor(),
                new RabbitTemplate()
        );
    }

    @Test
    @DisplayName("거래하기 요청을 통해 채팅방을 (없다면 생성 후) 조회할 수 있다.")
    void getChatRoomByTradeRequest() {
        // given
        Long usedItemId = 1L;
        Long ownerId = 1L;
        Long participantId = 2L;
        // when
        ChatMessageSliceResponse chatMessageSliceResponse = chatService.getChatRoomByTradeRequest(usedItemId, ownerId, participantId);
        // then
        assertThat(chatMessageSliceResponse.getChatRoomId()).isEqualTo(1L);
        assertThat(chatMessageSliceResponse.getContent()).isEqualTo(List.of());
        assertThat(chatMessageSliceResponse.getSize()).isEqualTo(10);
        assertThat(chatMessageSliceResponse.getNumberOfElements()).isZero();
        assertThat(chatMessageSliceResponse.isHasNext()).isFalse();
    }

    /**
     * 서비스단 변경에 유의
     */
    @Test
    @DisplayName("채팅을 전송할 수 있다.")
    void send() {
        // given
        connectionRepository.connect(1L, 1L);
        connectionRepository.connect(1L, 2L);
        ChatMessage chatMessage = ChatMessage.create(1L, 1L, "content", false);
        // when
        boolean isChatPartnerConnected = chatConnectionManager.isChatPartnerConnected(1L);
        ChatMessage newChatMessage = chatMessageRepository.save(chatMessage);

        // then
        assertThat(isChatPartnerConnected).isTrue();
        assertThat(newChatMessage.getContent()).isEqualTo("content");

    }

    @Test
    @DisplayName("채팅방목록들을 최근 메시지와 함께 조회할 수 있다.")
    void getChatRooms() {
        // given
        Long usedItemId = 1L;
        Long ownerId = 1L;
        Long participantId = 2L;
        chatService.getChatRoomByTradeRequest(usedItemId, ownerId, participantId);
        ChatMessage chatMessage = ChatMessage.create(1L, 1L, "content", false);
        ChatMessage chatMessage2 = ChatMessage.create(1L, 1L, "content2", false);
        chatMessageRepository.save(chatMessage);
        chatMessageRepository.save(chatMessage2);
        // when
        SliceResponse<ChatRoomListResponse> response = chatService.getChatRooms(ownerId, null);
        // then
        assertThat(response.getContent().size()).isEqualTo(2);
        assertThat(response.getNumberOfElements()).isEqualTo(2);
        assertThat(response.getContent().get(0).getLastMessage()).isEqualTo("content2");
        assertThat(response.getContent().get(0).getChatRoomId()).isEqualTo(1L);

    }
}
