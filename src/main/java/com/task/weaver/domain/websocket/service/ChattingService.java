package com.task.weaver.domain.websocket.service;

import com.task.weaver.domain.websocket.dto.ChattingRequest;
import com.task.weaver.domain.websocket.dto.ChattingRoomResponse;
import org.springframework.http.ResponseEntity;

public interface ChattingService {
    void save(Long chattingId, ChattingRequest chattingRequest);

    ChattingRoomResponse createChattingRoom();
}
