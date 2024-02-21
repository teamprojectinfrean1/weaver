package com.task.weaver.domain.websocket.dto.request;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RequestCreateChatting {

    private Long senderId;
    private String content;
}
