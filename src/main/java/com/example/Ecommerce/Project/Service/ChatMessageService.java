package com.example.Ecommerce.Project.Service;

import com.example.Ecommerce.Project.Entity.ChatMessage;
import com.example.Ecommerce.Project.Repository.ChatMessageRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ChatMessageService {
    private final ChatMessageRepository chatMessageRepository;

    public ChatMessageService(ChatMessageRepository chatMessageRepository) {
        this.chatMessageRepository = chatMessageRepository;
    }

    public ChatMessage saveMessage(ChatMessage message){
        message.setCreatedAt(LocalDateTime.now());
        return chatMessageRepository.save(message);
    }
}
