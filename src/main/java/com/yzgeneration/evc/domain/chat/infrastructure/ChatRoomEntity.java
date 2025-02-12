package com.yzgeneration.evc.domain.chat.infrastructure;

import com.yzgeneration.evc.domain.chat.model.ChatRoom;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Entity
@Builder
@Table(name = "chat_rooms")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ChatRoomEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String usedItemName;
    private Long ownerId;
    private Long senderId;
    private LocalDateTime createdAt;

    public static ChatRoomEntity create(ChatRoom chatRoom) {
        return ChatRoomEntity.builder()
                .usedItemName(chatRoom.getUsedItemName())
                .ownerId(chatRoom.getOwnerId())
                .senderId(chatRoom.getSenderId())
                .createdAt(LocalDateTime.now())
                .build();
    }

    public ChatRoom toModel() {
        return ChatRoom.builder()
                .id(id)
                .usedItemName(usedItemName)
                .ownerId(ownerId)
                .senderId(senderId)
                .createdAt(createdAt)
                .build();
    }
}
