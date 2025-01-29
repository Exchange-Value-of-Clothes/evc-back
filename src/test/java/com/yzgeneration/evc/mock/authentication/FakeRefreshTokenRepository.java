package com.yzgeneration.evc.mock.authentication;

import com.yzgeneration.evc.authentication.service.port.RefreshTokenRepository;

import java.util.HashMap;
import java.util.Map;

public class FakeRefreshTokenRepository implements RefreshTokenRepository {

    private final Map<Long, String> data = new HashMap<>();

    @Override
    public void save(Long memberId, String token) {
        data.put(memberId, token);
    }

    @Override
    public String getByMemberId(Long memberId) {
        return data.get(memberId);
    }
}
