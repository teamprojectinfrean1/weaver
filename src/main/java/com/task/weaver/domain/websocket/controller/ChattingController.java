package com.task.weaver.domain.websocket.controller;

import com.task.weaver.common.response.DataResponse;
import com.task.weaver.domain.websocket.dto.ChattingResponse;
import com.task.weaver.domain.websocket.service.ChattingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/chatting")
@RequiredArgsConstructor
public class ChattingController {
    private final ChattingService chattingService;

    @GetMapping("/{chattingRoomId}")
    public ResponseEntity<DataResponse<List<ChattingResponse>>> getChattingsInChattingRoom(@PathVariable(name = "chattingRoomId") Long chattingRoomId){
        List<ChattingResponse> chattingResponseList = chattingService.getChattings(chattingRoomId);

        return new ResponseEntity<>(DataResponse.of(HttpStatus.OK, "채팅 조회 성공", chattingResponseList), HttpStatus.CREATED);
    }



}
