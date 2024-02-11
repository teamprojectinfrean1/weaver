package com.task.weaver.domain.websocket.chatroom.repository;

import com.task.weaver.domain.websocket.chatroom.ChatRoom;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatRoomRepository extends MongoRepository<ChatRoom, String>{
    Optional<ChatRoom> findBySenderIdAndReceiverId(String senderId, String receiverId);
}
