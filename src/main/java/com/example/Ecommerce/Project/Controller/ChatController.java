package com.example.Ecommerce.Project.Controller;
import com.example.Ecommerce.Project.Entity.ChatMessage;
import com.example.Ecommerce.Project.Service.ChatMessageService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import com.example.Ecommerce.Project.Service.ChatMessageService;


@Controller
public class ChatController {

    private final ChatMessageService chatMessageService;

    public ChatController(
            ChatMessageService chatMessageService) {

        this.chatMessageService = chatMessageService;
    }

    @MessageMapping("/sendMessage")
    @SendTo("/topic/messages")
    public ChatMessage sendMessage(ChatMessage message) {

        return chatMessageService.saveMessage(message);
    }
}