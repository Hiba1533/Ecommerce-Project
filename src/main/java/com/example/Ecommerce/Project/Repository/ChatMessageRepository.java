package com.example.Ecommerce.Project.Repository;

import com.example.Ecommerce.Project.Entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageRepository
        extends JpaRepository<ChatMessage, Long> {
}