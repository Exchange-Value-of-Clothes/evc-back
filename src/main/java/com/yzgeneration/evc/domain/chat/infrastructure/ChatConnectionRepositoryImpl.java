package com.yzgeneration.evc.domain.chat.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
@RequiredArgsConstructor
public class ChatConnectionRepositoryImpl implements ChatConnectionRepository {

    private final StringRedisTemplate onlineMembers;
    private static final String CHAT_ROOM_PREFIX = "chatRoomId:";

    @Override
    public void connect(Long chatRoomId, Long memberId) {
        onlineMembers.opsForSet().add(CHAT_ROOM_PREFIX+ chatRoomId, String.valueOf(memberId)); // redis는 문자열 기반 데이터 저장소
        System.out.println("ChatConnectionRepositoryImpl.connect");
    }

    @Override
    public Long getOnlineMemberCount(Long chatRoomId) {
        return onlineMembers.opsForSet().size(CHAT_ROOM_PREFIX + chatRoomId);
    }

    @Override
    public void disconnect(Long chatRoomId, Long memberId) {
        onlineMembers.opsForSet().remove(CHAT_ROOM_PREFIX+chatRoomId, String.valueOf(memberId));
    }

}
