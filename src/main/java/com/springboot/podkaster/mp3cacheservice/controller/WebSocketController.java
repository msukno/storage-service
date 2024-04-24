package com.springboot.podkaster.mp3cacheservice.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    @MessageMapping("/message")
    @SendTo("/topic/progress")
    public String processMessage(String message) {

        System.out.println("message received from FE. Now returning a message back to FE");

        return "Received at server: " + message;
    }
}