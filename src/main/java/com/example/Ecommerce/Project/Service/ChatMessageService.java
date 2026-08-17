package com.example.Ecommerce.Project.Service;

import com.example.Ecommerce.Project.Entity.ChatMessage;
import com.example.Ecommerce.Project.Entity.Users;
import com.example.Ecommerce.Project.Repository.ChatMessageRepository;
import com.example.Ecommerce.Project.Repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ChatMessageService {
    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;

    public ChatMessageService(ChatMessageRepository chatMessageRepository, UserRepository userRepository) {
        this.chatMessageRepository = chatMessageRepository;
        this.userRepository = userRepository;
    }


    //saving msg content with sender and receiver and with time in db
    public ChatMessage saveMessage(ChatMessage message){
        Users sender = userRepository.findById(message.getSender().getId())
                .orElseThrow(() -> new RuntimeException("Sender not found"));

        Users receiver = userRepository.findById(message.getReceiver().getId())
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        message.setSender(sender);
        message.setReceiver(receiver);
        message.setCreatedAt(LocalDateTime.now());

        return chatMessageRepository.save(message);
    }
}