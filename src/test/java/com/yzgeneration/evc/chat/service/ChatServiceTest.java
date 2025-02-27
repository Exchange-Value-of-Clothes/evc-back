package com.yzgeneration.evc.chat.service;

import com.yzgeneration.evc.domain.chat.dto.Chatting;
import com.yzgeneration.evc.domain.chat.implement.ChatConnectionManager;
import com.yzgeneration.evc.domain.chat.implement.SessionAttributeAccessor;
import com.yzgeneration.evc.domain.chat.infrastructure.ChatConnectionRepository;
import com.yzgeneration.evc.domain.chat.infrastructure.ChatMemberRepository;
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
    @DisplayName("중고상품 아이디와 소유자 아이디를 통해 채팅방과 채팅멤버를 생성할 수 있다.")
    void createChatRoomAndChatMember() {
        // given
        Long usedItemId = 1L;
        Long ownerId = 1L;
        // when
        // then
        chatService.createChatRoomAndChatMember(usedItemId, ownerId);
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

    }
}
