package com.yzgeneration.evc.authentication.infrastructure;

import com.yzgeneration.evc.authentication.service.port.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRepositoryImpl implements RefreshTokenRepository {

    private final RedisTemplate<String, String> redisTemplate;
    private static final String REFRESH_TOKEN_PREFIX = "memberId:";

    @Override
    public void save(Long memberId, String token) {
        redisTemplate.opsForValue().set(REFRESH_TOKEN_PREFIX+memberId, token, Duration.ofDays(1));
    }

    @Override
    public String getByMemberId(Long memberId) {
        return redisTemplate.opsForValue().get(REFRESH_TOKEN_PREFIX+memberId);
    }
}
