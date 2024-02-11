package com.task.weaver.domain.websocket.chat.service;

import com.task.weaver.domain.websocket.chat.ChatMessage;

import java.util.List;

public interface ChatMessageService {
    List<ChatMessage> findChatMessages(String senderId, String recipientId);
    ChatMessage save(ChatMessage chatMessage);
}
