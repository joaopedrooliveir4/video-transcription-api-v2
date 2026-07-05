package com.transcription.infra.client;

import com.transcription.core.application.gateway.TranscriptionCacheGateway;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class TranscriptionCacheGatewayImpl implements TranscriptionCacheGateway {

    private final RedisTemplate<String, String> redisTemplate;

    public TranscriptionCacheGatewayImpl(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean exists(String sourceHash) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(sourceHash));
    }

    @Override
    public void save(String sourceHash) {
        redisTemplate.opsForValue().set(sourceHash, "1");
    }
}