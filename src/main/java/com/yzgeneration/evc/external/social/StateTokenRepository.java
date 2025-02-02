package com.yzgeneration.evc.external.social;

import com.yzgeneration.evc.exception.CustomException;
import com.yzgeneration.evc.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class StateTokenRepository implements CsrfRepository {

    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void save(String token, String socialPlatform) {
        redisTemplate.opsForValue().set(token, socialPlatform, Duration.ofMinutes(5));
    }

    @Override
    public void valid(String token, String socialPlatform) {
        if(redisTemplate.hasKey(token) && Objects.equals(redisTemplate.opsForValue().get(token), socialPlatform)) {
            redisTemplate.delete(token);
            return;
        }
        throw new CustomException(ErrorCode.INVALID_CSRF);
    }
}
