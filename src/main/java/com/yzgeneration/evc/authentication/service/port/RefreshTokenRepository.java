package com.yzgeneration.evc.authentication.service.port;

public interface RefreshTokenRepository {
    void save(Long memberId, String token);
    String getByMemberId(Long memberId);
}
