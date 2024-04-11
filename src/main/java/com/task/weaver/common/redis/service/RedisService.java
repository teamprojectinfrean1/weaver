package com.task.weaver.common.redis.service;

import static com.task.weaver.common.exception.ErrorCode.REFRESH_JWT_EXPIRED;

import com.task.weaver.common.redis.RefreshToken;
import com.task.weaver.common.redis.RefreshTokenRedisRepository;
import io.jsonwebtoken.JwtException;
import java.time.Duration;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisService {

    private final RefreshTokenRedisRepository refreshTokenRedisRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    public void setValues(String key, String data, Duration duration) {
        ValueOperations<String, Object> values = redisTemplate.opsForValue();
        values.set(key, data, duration);
    }

    @Transactional(readOnly = true)
    public String getValues(String key) {
        ValueOperations<String, Object> values = redisTemplate.opsForValue();
        if (values.get(key) == null) {
            return "false";
        }
        return (String) values.get(key);
    }

    public boolean checkExistsValue(String value) {
        return !value.equals("false");
    }

    @Transactional
    public void saveRefreshToken(UUID memberId, String refreshToken) {
        refreshTokenRedisRepository.save(new RefreshToken(memberId, refreshToken));
    }

    @Transactional(readOnly = true)
    public UUID findMemberByToken(String refreshToken) {
        RefreshToken token = refreshTokenRedisRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new JwtException(REFRESH_JWT_EXPIRED.getMessage()));
        return token.getId();
    }
}
