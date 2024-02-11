package com.task.weaver.domain.websocket.chatroom.service;

import org.springframework.stereotype.Service;

import java.util.Optional;


public interface ChatRoomService {
    Optional<String> getChatRoomId(String senderId, String receiverId, boolean createIfNotExist);
}
