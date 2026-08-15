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

    public ChatMessage saveMessage(ChatMessage message){

        // The frontend only sends { id } for sender/receiver, so load the
        // full Users rows here — otherwise username/email come back null
        // in the broadcast and the UI can't show who sent what.
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