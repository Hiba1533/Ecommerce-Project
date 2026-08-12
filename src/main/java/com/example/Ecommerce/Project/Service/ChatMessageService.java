package com.example.Ecommerce.Project.Service;

import com.example.Ecommerce.Project.Entity.ChatMessage;
import com.example.Ecommerce.Project.Repository.ChatMessageRepository;
import org.springframework.stereotype.Service;

@Service
public class ChatMessageService {
    private final ChatMessageRepository chatMessageRepository;

    public ChatMessageService(ChatMessageRepository chatMessageRepository) {
        this.chatMessageRepository = chatMessageRepository;
    }

    public ChatMessage saveMessage(ChatMessage message){
        return chatMessageRepository.save(message);
    }
}
