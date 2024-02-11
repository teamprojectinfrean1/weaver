package com.task.weaver.domain.websocket.chatroom;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Builder
@Getter
@Document
public class ChatRoom {
    private String chatRoomId;
    private String chatId;
    private String senderId;
    private String receiverId;
}
