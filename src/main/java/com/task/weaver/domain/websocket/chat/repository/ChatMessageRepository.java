package com.task.weaver.domain.websocket.chat.repository;

import com.task.weaver.domain.websocket.chat.ChatMessage;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ChatMessageRepository extends MongoRepository<ChatMessage, String>{
    List<ChatMessage> findByChatId(String chatId);
}
