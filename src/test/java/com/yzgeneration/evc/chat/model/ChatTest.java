package com.yzgeneration.evc.chat.model;

import com.yzgeneration.evc.domain.chat.model.ChatMember;
import com.yzgeneration.evc.domain.chat.model.ChatMessage;
import com.yzgeneration.evc.domain.chat.model.ChatRoom;
import com.yzgeneration.evc.domain.item.enums.ItemType;
import com.yzgeneration.evc.fixture.ChatFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

class ChatTest {

    @Test
    @DisplayName("중고 상품의 아이디와 소유자의 아이디로 채팅방을 생성할 수 있다.")
    void createChatRoom() {
        // given
        Long itemId = 1L;
        ItemType itemType = ItemType.USEDITEM;
        Long ownerId = 1L;
        Long participantId = 2L;

        // when
        ChatRoom chatRoom = ChatRoom.create(itemId, ownerId, participantId, itemType);

        // then
        assertThat(chatRoom.getItemId()).isEqualTo(itemId);
        assertThat(chatRoom.getOwnerId()).isEqualTo(ownerId);
        assertThat(chatRoom.getParticipantId()).isEqualTo(participantId);
        assertThat(chatRoom.getItemType()).isEqualTo(itemType);
    }

    @Test
    @DisplayName("채팅방 아이디와 멤버 아이디로 채팅방 멤버를 생성할 수 있다.")
    void createChatMember() {
        // given
        Long chatRoomId = 1L;
        Long memberId = 1L;

        // when
        ChatMember chatMember = ChatMember.create(chatRoomId, memberId);

        // then
        assertThat(chatMember.getChatRoomId()).isEqualTo(chatRoomId);
        assertThat(chatMember.getMemberId()).isEqualTo(memberId);
        assertThat(chatMember.getIsDeleted()).isFalse();
    }

    @Test
    @DisplayName("채팅메시지를 생성할 수 있다.")
    void createChatMessage() {
        // given
        Long chatRoomId = 1L;
        Long senderId = 1L;
        String content = "내용";

        // when
        ChatMessage chatMessage = ChatMessage.create(chatRoomId, senderId, content, false, LocalDateTime.MIN);

        // then
        assertThat(chatMessage.getChatRoomId()).isEqualTo(chatRoomId);
        assertThat(chatMessage.getSenderId()).isEqualTo(senderId);
        assertThat(chatMessage.getContent()).isEqualTo(content);
        assertThat(chatMessage.getIsRead()).isFalse();
    }

    @Test
    @DisplayName("채팅멤버를 논리적으로 삭제할 수 있다.")
    void deleteChatMember() {
        // given
        ChatMember chatMember = ChatFixture.chatMember(1L, 1L);

        // when
        chatMember.exit();

        // then
        assertThat(chatMember.getIsDeleted()).isTrue();
    }
}
