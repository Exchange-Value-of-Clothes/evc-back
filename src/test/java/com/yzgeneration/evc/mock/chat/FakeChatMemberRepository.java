package com.yzgeneration.evc.mock.chat;

import com.yzgeneration.evc.domain.chat.infrastructure.ChatMemberRepository;
import com.yzgeneration.evc.domain.chat.model.ChatMember;
import com.yzgeneration.evc.exception.CustomException;
import com.yzgeneration.evc.exception.ErrorCode;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FakeChatMemberRepository implements ChatMemberRepository {

    private Long autoGeneratedId = 0L;
    private final List<ChatMember> data = new ArrayList<>();

    public int getDataSize() {
        return data.size();
    }

        @Override
    public ChatMember save(ChatMember chatMember) {
        if (chatMember.getId() == null || chatMember.getId() == 0) {
            ChatMember newChatMember = ChatMember.builder()
                    .id(++autoGeneratedId)
                    .chatRoomId(chatMember.getChatRoomId())
                    .memberId(chatMember.getMemberId())
                    .isDeleted(chatMember.getIsDeleted())
                    .build();
            data.add(newChatMember);
            return newChatMember;
        } else {
            data.removeIf(item -> Objects.equals(item.getId(), chatMember.getId()));
            data.add(chatMember);
            return chatMember;
        }
    }

    @Override
    public List<ChatMember> saveAll(List<ChatMember> chatMembers) {
        List<ChatMember> savedMembers = new ArrayList<>();
        for (ChatMember chatMember : chatMembers) {
            savedMembers.add(save(chatMember));
        }
        return savedMembers;
    }

    @Override
    public ChatMember get(Long chatRoomId, Long memberId) {
        for (ChatMember chatMember : data) {
            if (chatMember.getChatRoomId().equals(chatRoomId) && chatMember.getMemberId().equals(memberId)) return chatMember;
        }
        throw new CustomException(ErrorCode.CHAT_MEMBER_NOT_FOUND);
    }

    @Override
    public List<ChatMember> getDeletedChatMembers(Long chatRoomId, List<Long> memberIds) {
        List<ChatMember> deletedMembers = new ArrayList<>();
        for (ChatMember chatMember : data) {
            if (memberIds.contains(chatMember.getMemberId())) deletedMembers.add(chatMember);
        }
        return deletedMembers;
    }

}
