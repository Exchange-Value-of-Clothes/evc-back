package com.yzgeneration.evc.external.social.infrastructure;

import com.yzgeneration.evc.common.exception.CustomException;
import com.yzgeneration.evc.common.exception.ErrorCode;
import com.yzgeneration.evc.external.social.CsrfRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StateTokenRepository implements CsrfRepository {

    private final RedisTemplate<String, String> redisTemplate;


    @Override
    public void save(String token) {
        redisTemplate.opsForValue().set(token, token);
    }

    @Override
    public void valid(String token) {
        if(redisTemplate.hasKey(token)) {
            redisTemplate.delete(token);
            return;
        }
        throw new CustomException(ErrorCode.INVALID_CSRF);
    }
}
