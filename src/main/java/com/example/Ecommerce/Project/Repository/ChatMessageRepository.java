package com.example.Ecommerce.Project.Repository;

import com.example.Ecommerce.Project.Entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long>{

    List<ChatMessage> findBySenderIdAndReceiverId(Long senderId, Long receiverId);

}
