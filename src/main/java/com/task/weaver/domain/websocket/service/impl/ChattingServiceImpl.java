package com.task.weaver.domain.websocket.service.impl;

import com.task.weaver.common.exception.BusinessException;
import com.task.weaver.domain.user.entity.User;
import com.task.weaver.domain.user.repository.UserRepository;
import com.task.weaver.domain.websocket.dto.ChattingRequest;
import com.task.weaver.domain.websocket.dto.ChattingRoomResponse;
import com.task.weaver.domain.websocket.entity.Chatting;
import com.task.weaver.domain.websocket.entity.ChattingRoom;
import com.task.weaver.domain.websocket.repository.ChattingRoomRepository;
import com.task.weaver.domain.websocket.service.ChattingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChattingServiceImpl implements ChattingService {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final UserRepository userRepository;
    private final ChattingRoomRepository chattingRoomRepository;
    @Override
    @Transactional
    public void save(Long chattingId, ChattingRequest chattingRequest) throws BusinessException {
        User sender = userRepository.findById(chattingRequest.getSenderId())
                .orElseThrow();
        simpMessagingTemplate.convertAndSend("/subscription/chattings/" + chattingId, chattingRequest.getContent());
        ChattingRoom chattingRoom = chattingRoomRepository.findById(chattingId)
                .orElseThrow();
        Chatting chatting = Chatting.builder()
                .user(sender)
                .chattingRoom(chattingRoom)
                .content(chattingRequest.getContent())
                .build();
        chattingRoom.createChatting(chatting);
        chattingRoomRepository.save(chattingRoom);
    }

    @Override
    public List<ChattingRoomResponse> getChattingRoomsByUserId(Long userId) throws BusinessException {
        return null;
    }

    @Override
    public ChattingRoomResponse createChattingRoom() throws BusinessException {
        ChattingRoom chattingRoom = ChattingRoom.builder().build();
        ChattingRoom savedChattingRoom = chattingRoomRepository.save(chattingRoom);
        return ChattingRoomResponse.builder()
                .chattingRoomId(savedChattingRoom.getId())
                .build();
    }
}
