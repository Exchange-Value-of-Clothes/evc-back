package com.yzgeneration.evc.mock.chat;

import com.yzgeneration.evc.domain.chat.infrastructure.ChatConnectionRepository;

import java.util.*;

public class FakeChatConnectionRepository implements ChatConnectionRepository {

    private final Map<String, Set<String>> data = new HashMap<>();
    private static final String CHAT_ROOM_PREFIX = "chatRoomId:";
    @Override
    public void connect(Long chatRoomId, Long memberId) {
        data.computeIfAbsent(CHAT_ROOM_PREFIX + chatRoomId, k -> new HashSet<>())
                .add(String.valueOf(memberId));
    }

    @Override
    public Long getOnlineMemberCount(Long chatRoomId) {
        return (long) data.getOrDefault(CHAT_ROOM_PREFIX + chatRoomId, Collections.emptySet()).size();
    }

    @Override
    public void disconnect(Long chatRoomId, Long memberId) {

    }
}
