package com.springboot.podkaster.mp3cacheservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebSocketTriggerController {

    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public WebSocketTriggerController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @GetMapping("/trigger")
    public String triggerMessage() {
        messagingTemplate.convertAndSend("/topic/progress", "Test Message from BE");
        return "Message sent!";
    }
}