package com.springboot.podkaster.mp3cacheservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class WebSocketService {

    private final SimpMessagingTemplate template;

    @Autowired
    public WebSocketService(SimpMessagingTemplate template) {
        this.template = template;
    }

    @Scheduled(fixedRate = 1000)
    public void sendMessage() {
        template.convertAndSend("/topic/progress", "Hello World");
    }
}
