package com.task.weaver.domain.websocket.chatroom.service.impl;

import com.task.weaver.domain.websocket.chatroom.ChatRoom;
import com.task.weaver.domain.websocket.chatroom.repository.ChatRoomRepository;
import com.task.weaver.domain.websocket.chatroom.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatRoomServiceImpl implements ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;

    @Override
    public Optional<String> getChatRoomId(String senderId, String receiverId, boolean createIfNotExist) {
        return chatRoomRepository.findBySenderIdAndReceiverId(senderId, receiverId)
                .map(ChatRoom::getChatRoomId)
                .or(() -> {
                    if (createIfNotExist) {
                        String chatId = createChatId(senderId, receiverId);
                        return Optional.of(chatId);
                    } else {
                        return Optional.empty();
                    }
                });
    }

    private String createChatId(String senderId, String receiverId) {
        String chatId = String.format("%s_%s", senderId, receiverId);
        ChatRoom senderRecipient = ChatRoom.builder()
                .chatId(chatId)
                .senderId(senderId)
                .receiverId(receiverId)
                .build();
        ChatRoom recipientSender = ChatRoom.builder()
                .chatId(chatId)
                .senderId(receiverId)
                .receiverId(senderId)
                .build();
        chatRoomRepository.save(senderRecipient);
        chatRoomRepository.save(recipientSender);
        return chatId;
    }
}
