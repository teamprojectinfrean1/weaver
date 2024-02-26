package com.task.weaver.domain.websocket.controller;

import com.task.weaver.domain.websocket.dto.ChattingRoomResponse;
import com.task.weaver.domain.websocket.service.ChattingService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chattingRooms")
public class ChattingRoomController {
    private final ChattingService chattingService;

    @PostMapping("/{project_id}")
    @Parameter(name = "project_id", in = ParameterIn.PATH )
    public ResponseEntity<ChattingRoomResponse> createChattingRoom(@PathVariable(name = "project_id") Long project_id) {
        ChattingRoomResponse chattingRoom = chattingService.createChattingRoom(project_id);
        return ResponseEntity.ok(chattingRoom);
    }
}