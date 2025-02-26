package com.yzgeneration.evc.domain.chat.infrastructure;

import com.yzgeneration.evc.domain.chat.model.ChatMember;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@Builder
@Table(name = "chat_members")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ChatMemberEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long chatRoomId;
    private Long memberId;
    private Boolean isDeleted;

    public static ChatMemberEntity from(ChatMember chatMember) {
        return ChatMemberEntity.builder()
                .chatRoomId(chatMember.getChatRoomId())
                .memberId(chatMember.getMemberId())
                .isDeleted(chatMember.getIsDeleted())
                .build();
    }

    public ChatMember toModel() {
        return ChatMember.builder()
                .id(id)
                .chatRoomId(chatRoomId)
                .memberId(memberId)
                .isDeleted(isDeleted).build();
    }
}
