package com.task.weaver.domain.websocket.dto.request;

import lombok.*;

import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RequestCreateChatting {

    private UUID senderId;
    private String content;
}
