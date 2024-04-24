package com.springboot.podkaster.mp3cacheservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisService {

    private final RedisTemplate<String, String> redisTemplate;

    @Autowired
    public RedisService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public String getValue(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void setValue(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
        redisTemplate.convertAndSend("redisChanges", "update");
    }

    public String removeValue(String key) {
        // First, get the value.
        String value = redisTemplate.opsForValue().get(key);
        // Then, delete the key.
        redisTemplate.delete(key);
        // Return the retrieved value.
        return value;
    }
}

