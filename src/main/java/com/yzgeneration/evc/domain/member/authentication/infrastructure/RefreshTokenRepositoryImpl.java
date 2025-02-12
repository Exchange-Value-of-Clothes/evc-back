package com.yzgeneration.evc.domain.member.authentication.infrastructure;

import com.yzgeneration.evc.domain.member.authentication.service.port.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRepositoryImpl implements RefreshTokenRepository {

    private final StringRedisTemplate redisTemplate;
    private static final String REFRESH_TOKEN_PREFIX = "memberId:";

    @Override
    public void save(Long key, String token) {
        redisTemplate.opsForValue().set(REFRESH_TOKEN_PREFIX+ key, token, Duration.ofDays(1));
    }

    @Override
    public String getByMemberId(Long memberId) {
        return redisTemplate.opsForValue().get(REFRESH_TOKEN_PREFIX+memberId);
    }
}
