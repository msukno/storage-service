package com.springboot.podkaster.mp3cacheservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
public class RedisMessageSubscriber implements MessageListener {

    private final RedisTemplate<String, String> redisTemplate;
    private final SimpMessagingTemplate websocketTemplate;

    @Autowired
    public RedisMessageSubscriber(RedisTemplate<String, String> redisTemplate, SimpMessagingTemplate websocketTemplate) {
        this.redisTemplate = redisTemplate;
        this.websocketTemplate = websocketTemplate;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        // When a message is received, fetch all key-value pairs from Redis
        Set<String> keys = redisTemplate.keys("*");
        Map<String, String> allValues = new HashMap<>();
        if (keys != null) {
            for (String key : keys) {
                allValues.put(key, redisTemplate.opsForValue().get(key));
            }
        }
        // Send the collected key-value pairs to the WebSocket topic
        websocketTemplate.convertAndSend("/topic/progress", allValues);
    }
}