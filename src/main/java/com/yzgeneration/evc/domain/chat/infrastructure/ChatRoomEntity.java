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
    private Long usedItemId;
    private Long ownerId;
    private Long participationId;
    private LocalDateTime createdAt;

    public static ChatRoomEntity from(ChatRoom chatRoom) {
        return ChatRoomEntity.builder()
                .id(chatRoom.getId())
                .usedItemId(chatRoom.getUsedItemId())
                .ownerId(chatRoom.getOwnerId())
                .participationId(chatRoom.getParticipationId())
                .createdAt(LocalDateTime.now())
                .build();
    }

    public ChatRoom toModel() {
        return ChatRoom.builder()
                .id(id)
                .usedItemId(usedItemId)
                .ownerId(ownerId)
                .participationId(participationId)
                .createdAt(createdAt)
                .build();
    }
}
