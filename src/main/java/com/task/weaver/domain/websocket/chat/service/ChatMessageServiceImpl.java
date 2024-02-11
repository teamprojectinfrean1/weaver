package com.task.weaver.domain.websocket.chat.service;

import com.task.weaver.domain.websocket.chat.ChatMessage;
import com.task.weaver.domain.websocket.chat.repository.ChatMessageRepository;
import com.task.weaver.domain.websocket.chatroom.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatMessageServiceImpl implements ChatMessageService{

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomService chatRoomService;

    @Override
    public ChatMessage save(ChatMessage chatMessage){
        String chatId = chatRoomService
                .getChatRoomId(chatMessage.getSenderId(), chatMessage.getRecipientId(), true)
                .orElseThrow();
        chatMessage.setChatId(chatId);
        return chatMessageRepository.save(chatMessage);
    }
    @Override
    public List<ChatMessage> findChatMessages(String senderId, String recipientId){
        String chatId = chatRoomService.getChatRoomId(senderId, recipientId, false).orElseThrow();
        List<ChatMessage> messages = chatMessageRepository.findByChatId(chatId);
        if(!messages.isEmpty()){
            return messages;
        }else{
            return new ArrayList<>();
        }
    }
}
