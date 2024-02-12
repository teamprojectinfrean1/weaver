package com.task.weaver.domain.websocket.chatroom;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Entity
public class ChatRoom {
    @Id
    private String chatRoomId;
    private String chatId;
    private String senderId;
    private String receiverId;
}
