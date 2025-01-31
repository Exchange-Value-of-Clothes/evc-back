package com.yzgeneration.evc.domain.member.authentication.service.port;

public interface RefreshTokenRepository {
    void save(Long key, String value);
    String getByMemberId(Long memberId);
}
