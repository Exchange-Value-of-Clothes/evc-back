package com.yzgeneration.evc.mock.authentication;

import com.yzgeneration.evc.authentication.service.port.RefreshTokenRepository;

import java.util.HashMap;
import java.util.Map;

public class FakeRefreshTokenRepository implements RefreshTokenRepository {

    private final Map<Long, String> data = new HashMap<>();

    @Override
    public void save(Long key, String token) {
        data.put(key, token);
    }

    @Override
    public String getByMemberId(Long memberId) {
        return data.get(memberId);
    }
}
