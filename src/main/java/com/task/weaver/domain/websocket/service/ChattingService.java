package com.task.weaver.domain.websocket.service;

import com.task.weaver.domain.websocket.dto.request.RequestCreateChatting;
import com.task.weaver.domain.websocket.dto.response.ResponseGetChattings;
import com.task.weaver.domain.websocket.dto.response.ResponseCreateChattingRoom;

import java.util.List;
import java.util.UUID;

public interface ChattingService {
    List<ResponseGetChattings> getChattings(Long projectId);
    void deleteChatting(Long chatting_id);
    void save(Long chattingId, RequestCreateChatting requestCreateChatting);

    ResponseCreateChattingRoom createChattingRoom(UUID projectId);

}
