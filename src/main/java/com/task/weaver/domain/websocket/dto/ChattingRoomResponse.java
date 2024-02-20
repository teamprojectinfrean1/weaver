package com.task.weaver.domain.websocket.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChattingRoomResponse {
    private Long chattingRoomId;
    private String chattingRoomName;
}
