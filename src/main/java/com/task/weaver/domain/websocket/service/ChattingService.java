package com.task.weaver.domain.websocket.service;

import com.task.weaver.domain.websocket.dto.ChattingRequest;

public interface ChattingService {
    void save(Long chattingId, ChattingRequest chattingRequest);
}
