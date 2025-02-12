package com.yzgeneration.evc.domain.chat.infrastructure;

public interface ChatConnectionRepository {
    void connect(Long chatRoomId, Long memberId);
    Long getOnlineMemberCount(Long chatRoomId);
    void disconnect(Long chatRoomId, Long memberId);

}
