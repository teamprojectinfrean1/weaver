package com.task.weaver.domain.websocket.service;

import com.task.weaver.domain.websocket.dto.ChattingRequest;
import com.task.weaver.domain.websocket.dto.ChattingResponse;
import com.task.weaver.domain.websocket.dto.ChattingRoomResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ChattingService {
    List<ChattingResponse> getChattings(Long project_id);


    void save(Long chattingId, ChattingRequest chattingRequest);

    ChattingRoomResponse createChattingRoom(Long project_id);
}
