package com.task.weaver.domain.websocket.controller;

import com.task.weaver.domain.websocket.dto.ChattingRoomResponse;
import com.task.weaver.domain.websocket.service.ChattingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChattingRoomController {
    private final ChattingService chattingService;

    @GetMapping("/chattings/")
    public ResponseEntity<ChattingRoomResponse> createChattingRoom() {
        ChattingRoomResponse chattingRoom = chattingService.createChattingRoom();
        return ResponseEntity.ok(chattingRoom);
    }
}
