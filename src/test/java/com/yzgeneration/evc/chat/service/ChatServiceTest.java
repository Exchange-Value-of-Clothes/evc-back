package com.yzgeneration.evc.chat.service;

import com.yzgeneration.evc.common.dto.SliceResponse;
import com.yzgeneration.evc.domain.chat.dto.ChatMessageSliceResponse;
import com.yzgeneration.evc.domain.chat.dto.ChatRoomListResponse;
import com.yzgeneration.evc.domain.chat.implement.ChatConnectionManager;
import com.yzgeneration.evc.domain.chat.implement.ChatRoomManager;
import com.yzgeneration.evc.domain.chat.implement.SessionAttributeAccessor;
import com.yzgeneration.evc.domain.chat.infrastructure.ChatConnectionRepository;
import com.yzgeneration.evc.domain.chat.infrastructure.ChatMemberRepository;
import com.yzgeneration.evc.domain.chat.infrastructure.ChatMessageRepository;
import com.yzgeneration.evc.domain.chat.model.ChatMember;
import com.yzgeneration.evc.domain.chat.model.ChatMessage;
import com.yzgeneration.evc.domain.chat.service.ChatService;
import com.yzgeneration.evc.exception.CustomException;
import com.yzgeneration.evc.exception.ErrorCode;
import com.yzgeneration.evc.mock.chat.FakeChatConnectionRepository;
import com.yzgeneration.evc.mock.chat.FakeChatMemberRepository;
import com.yzgeneration.evc.mock.chat.FakeChatMessageRepository;
import com.yzgeneration.evc.mock.chat.FakeChatRoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

// TODO 본인 소유 채팅 확인 추가
class ChatServiceTest {

    private ChatService chatService;
    private ChatConnectionRepository connectionRepository;
    private ChatConnectionManager chatConnectionManager;
    private ChatMessageRepository chatMessageRepository;
    private FakeChatMemberRepository chatMemberRepository;

    @BeforeEach
    void init() {
        chatMemberRepository = new FakeChatMemberRepository();
        ChatRoomManager chatRoomManager = new ChatRoomManager(new FakeChatRoomRepository(),
                chatMemberRepository);
        connectionRepository = new FakeChatConnectionRepository();
        chatConnectionManager = new ChatConnectionManager(connectionRepository);
        chatMessageRepository = new FakeChatMessageRepository();
        chatService = new ChatService(
                chatRoomManager,
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
        ChatMessage chatMessage = ChatMessage.create(1L, 1L, "content", false, LocalDateTime.MIN);
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
        ChatMessage chatMessage = ChatMessage.create(1L, 1L, "content", false, LocalDateTime.MIN);
        ChatMessage chatMessage2 = ChatMessage.create(1L, 1L, "content2", false, LocalDateTime.now());
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

    /**
     * 채팅방목록 조회는 해당 채팅방 멤버의 isDeleted를 확인하면서 가져와서 상관없음
     * 채팅방조회는 채팅방목록 조회로부터 메시지를 가져오기 때문에 중복해서 isDeleted를 검증할 필요없음
     * */
    @Test
    @DisplayName("채팅방을 나갈 수 있다.")
    void exitChatRoom() {
        // given
        Long usedItemId = 1L;
        Long ownerId = 1L;
        Long participantId = 2L;
        ChatMessageSliceResponse chatMessageSliceResponse = chatService.getChatRoomByTradeRequest(usedItemId, ownerId, participantId);
        Long chatRoomId = chatMessageSliceResponse.getChatRoomId();
        // when
        chatService.exit(chatRoomId, participantId);

        // then
        ChatMember chatMember = chatMemberRepository.get(chatRoomId, participantId);
        assertThat(chatMember.getIsDeleted()).isTrue();

    }

    @Test
    @DisplayName("채팅방을 나가면 채팅방목록이 조회되지 않는다")
    void exitThenCantGetChatRooms() {
        // given
        Long usedItemId = 1L;
        Long ownerId = 1L;
        Long participantId = 2L;
        ChatMessageSliceResponse chatMessageSliceResponse = chatService.getChatRoomByTradeRequest(usedItemId, ownerId, participantId);
        ChatMessage chatMessage = ChatMessage.create(1L, 1L, "content", false, LocalDateTime.MIN);
        ChatMessage chatMessage2 = ChatMessage.create(1L, 1L, "content2", false, LocalDateTime.MIN);
        chatMessageRepository.save(chatMessage);
        chatMessageRepository.save(chatMessage2);
        Long chatRoomId = chatMessageSliceResponse.getChatRoomId();
        chatService.exit(chatRoomId, participantId);

        // when
        SliceResponse<ChatRoomListResponse> chatRooms = chatService.getChatRooms(participantId, null);

        // then
        assertThat(chatRooms.getContent().size()).isZero();
        assertThat(chatRooms.getNumberOfElements()).isZero();
    }

    /**
     * 채팅방멤버가 방을 나감 (isDeleted=true) 다시 거래하기를 요청할 때
     * 1. 채팅방이 삭제된 상태라면 채팅방멤버의 isDeleted를 false로 만들어서 주기 때문에 문제없음
     * 2. 기존 채팅방이 존재하면, 거래하기를 요청할 때 isDeleted=true인 채팅방멤버가 그대로 가져와짐. 여기를 수정해야함.
     */
    @Test
    @DisplayName("채팅방을 나간 후 다시 거래하기를 요청한다.")
    void exitThenTradeRequest() {
        // given
        Long usedItemId = 1L;
        Long ownerId = 1L;
        Long participantId = 2L;
        ChatMessageSliceResponse chatMessageSliceResponse = chatService.getChatRoomByTradeRequest(usedItemId, ownerId, participantId);
        Long chatRoomId = chatMessageSliceResponse.getChatRoomId();
        chatService.exit(chatRoomId, participantId);

        // when
        chatService.getChatRoomByTradeRequest(usedItemId, ownerId, participantId);

        // then
        assertThat(chatMemberRepository.get(chatRoomId, 1L).getIsDeleted()).isFalse();
        assertThat(chatMemberRepository.get(chatRoomId, participantId).getIsDeleted()).isFalse();
        assertThatThrownBy(()->chatMemberRepository.get(chatRoomId, 3L))
                .isInstanceOf(CustomException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.CHAT_MEMBER_NOT_FOUND);

    }

    @Test
    @DisplayName("거래하기를 여러번 요청해도 채팅방멤버는 중복해서 생성되지 않는다")
    void notDuplicatedChatRoomMember() {
        // given
        Long usedItemId = 1L;
        Long ownerId = 1L;
        Long participantId = 2L;
        ChatMessageSliceResponse chatMessageSliceResponse = chatService.getChatRoomByTradeRequest(usedItemId, ownerId, participantId);
        Long chatRoomId = chatMessageSliceResponse.getChatRoomId();

        // when
        chatService.getChatRoomByTradeRequest(usedItemId, ownerId, participantId);

        // then
       assertThat(chatMemberRepository.getDataSize()).isEqualTo(2);


    }
}
