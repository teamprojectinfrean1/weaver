package com.task.weaver.domain.websocket.controller;

import com.task.weaver.common.response.DataResponse;
import com.task.weaver.common.response.MessageResponse;
import com.task.weaver.domain.websocket.dto.response.ResponseGetChattings;
import com.task.weaver.domain.websocket.service.ChattingService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Chatting Controller", description = "채팅 crud를 위한 컨트롤러")
@RestController
@RequestMapping("/api/v1/chatting")
@RequiredArgsConstructor
public class ChattingController {
    private final ChattingService chattingService;

    @GetMapping("/{chattingRoomId}")
    public ResponseEntity<DataResponse<List<ResponseGetChattings>>> getChattingsInChattingRoom(@PathVariable(name = "chattingRoomId") Long chattingRoomId){
        List<ResponseGetChattings> responseGetChattingsList = chattingService.getChattings(chattingRoomId);

        return new ResponseEntity<>(DataResponse.of(HttpStatus.OK, "채팅 조회 성공", responseGetChattingsList, true), HttpStatus.CREATED);
    }

    @DeleteMapping("/{chatting_Id}")
    public ResponseEntity<MessageResponse> deleteChatting(@PathVariable(name = "chatting_id") Long chatting_id){
        chattingService.deleteChatting(chatting_id);

        return new ResponseEntity<>(MessageResponse.of(HttpStatus.OK, "채팅 삭제 성공", true), HttpStatus.NO_CONTENT);
    }
}
