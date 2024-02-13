package com.task.weaver.domain.websocket.service.impl;

import com.task.weaver.domain.user.entity.User;
import com.task.weaver.domain.user.repository.UserRepository;
import com.task.weaver.domain.websocket.dto.ChattingRequest;
import com.task.weaver.domain.websocket.entity.Chatting;
import com.task.weaver.domain.websocket.entity.ChattingRoom;
import com.task.weaver.domain.websocket.repository.ChattingRoomRepository;
import com.task.weaver.domain.websocket.service.ChattingService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChattingServiceImpl implements ChattingService {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final UserRepository userRepository;
    private final ChattingRoomRepository chattingRoomRepository;
    @Override
    @Transactional
    public void save(Long chattingId, ChattingRequest chattingRequest) {
        User sender = userRepository.findById(chattingRequest.getSenderId())
                .orElseThrow(() -> new IllegalArgumentException("There's no member"));
        simpMessagingTemplate.convertAndSend("/subscription/chattings/" + chattingId, chattingRequest.getContent());
        ChattingRoom chattingRoom = chattingRoomRepository.findById(chattingId)
                .orElseThrow(() -> new IllegalArgumentException("There's no chatting room"));
        Chatting chatting = Chatting.builder()
                .user(sender)
                .chattingRoom(chattingRoom)
                .content(chattingRequest.getContent())
                .build();
        chattingRoom.createChatting(chatting);
        chattingRoomRepository.save(chattingRoom);
    }
}
