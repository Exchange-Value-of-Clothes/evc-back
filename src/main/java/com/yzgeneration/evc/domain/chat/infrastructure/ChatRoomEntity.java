package com.yzgeneration.evc.domain.chat.infrastructure;

import com.yzgeneration.evc.domain.chat.model.ChatRoom;
import com.yzgeneration.evc.domain.image.enums.ItemType;
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
    private Long itemId;
    private Long ownerId;
    private Long participantId;
    @Enumerated(EnumType.STRING)
    private ItemType itemType;
    private Boolean isDeleted;
    private LocalDateTime createdAt;

    public static ChatRoomEntity from(ChatRoom chatRoom) {
        return ChatRoomEntity.builder()
                .id(chatRoom.getId())
                .itemId(chatRoom.getItemId())
                .ownerId(chatRoom.getOwnerId())
                .participantId(chatRoom.getParticipantId())
                .itemType(chatRoom.getItemType())
                .isDeleted(chatRoom.getIsDeleted())
                .createdAt(LocalDateTime.now())
                .build();
    }

    public ChatRoom toModel() {
        return ChatRoom.builder()
                .id(id)
                .itemId(itemId)
                .ownerId(ownerId)
                .participantId(participantId)
                .itemType(itemType)
                .isDeleted(isDeleted)
                .createdAt(createdAt)
                .build();
    }
}
