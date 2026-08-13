package com.example.Ecommerce.Project.Controller;

import com.example.Ecommerce.Project.Entity.ChatMessage;
import com.example.Ecommerce.Project.Service.ChatMessageService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    private final ChatMessageService chatMessageService;
    private final SimpMessagingTemplate messagingTemplate;

    public ChatController(
            ChatMessageService chatMessageService,
            SimpMessagingTemplate messagingTemplate) {

        this.chatMessageService = chatMessageService;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/sendMessage")
    public void sendMessage(ChatMessage message) {

        ChatMessage saved =
                chatMessageService.saveMessage(message);

        messagingTemplate.convertAndSend(
                "/topic/messages",
                saved
        );
    }
}