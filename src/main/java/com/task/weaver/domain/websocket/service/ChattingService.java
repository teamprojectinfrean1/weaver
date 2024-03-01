package com.task.weaver.domain.websocket.service;

import com.task.weaver.domain.websocket.dto.request.RequestCreateChatting;
import com.task.weaver.domain.websocket.dto.response.ResponseGetChattings;
import com.task.weaver.domain.websocket.dto.response.ResponseCreateChattingRoom;

import java.util.List;

public interface ChattingService {
    List<ResponseGetChattings> getChattings(Long project_id);
    void deleteChatting(Long chatting_id);
    void save(Long chattingId, RequestCreateChatting requestCreateChatting);

    ResponseCreateChattingRoom createChattingRoom(Long project_id);

}
