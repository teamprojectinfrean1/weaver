package com.task.weaver.domain.websocket.controller;

import com.task.weaver.common.response.DataResponse;
import com.task.weaver.domain.websocket.dto.response.ResponseCreateChattingRoom;
import com.task.weaver.domain.websocket.service.ChattingService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "ChattingRoom Controller", description = "채팅방 관련 컨트롤러")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chattingRooms")
public class ChattingRoomController {
    private final ChattingService chattingService;

    @PostMapping("/{project_id}")
    @Parameter(name = "project_id", in = ParameterIn.PATH )
    public ResponseEntity<DataResponse<ResponseCreateChattingRoom>> createChattingRoom(@PathVariable(name = "project_id") Long project_id) {
        ResponseCreateChattingRoom chattingRoom = chattingService.createChattingRoom(project_id);
        return new ResponseEntity<>(DataResponse.of(HttpStatus.OK, "채팅방 생성 성공", chattingRoom), HttpStatus.CREATED);
    }
}
