package com.task.weaver.domain.websocket.dto.response;

import com.task.weaver.domain.websocket.entity.ChattingRoom;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseCreateChattingRoom {
    private Long chattingRoomId;
    private String chattingRoomName;

    public ResponseCreateChattingRoom(ChattingRoom chattingRoom){
        this.chattingRoomId = chattingRoom.getId();
        this.chattingRoomName = chattingRoom.getName();
    }
}
