package com.yzgeneration.evc.domain.chat.infrastructure;

import com.querydsl.core.annotations.QueryEntity;
import com.yzgeneration.evc.domain.chat.model.ChatMessage;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Builder
@Document(collection = "chat_message")
@QueryEntity
public class ChatMessageDocument {

    // javax.persistence는 관계형, 이건 Jpa에 지원되지 않는 nosql이나 프레임워크용
    @Id
    private String id;
    private Long chatRoomId;
    private Long senderId;
    private String content;
    private boolean read;
//    @Indexed
    private LocalDateTime createdAt;

    public static ChatMessageDocument from(ChatMessage chatMessage) {
        return ChatMessageDocument.builder()
                .chatRoomId(chatMessage.getChatRoomId())
                .senderId(chatMessage.getSenderId())
                .content(chatMessage.getContent())
                .read(chatMessage.getIsRead())
                .createdAt(LocalDateTime.now())
                .build();
    }

    public ChatMessage toModel() {
        return ChatMessage.builder()
                .chatRoomId(chatRoomId)
                .senderId(senderId)
                .content(content)
                .isRead(read)
                .createdAt(createdAt)
                .build();
    }
}
