package com.task.weaver.domain.websocket.controller;

import com.task.weaver.domain.websocket.dto.ChattingRequest;
import com.task.weaver.domain.websocket.service.ChattingService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChattingController {
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ChattingService chattingService;

    @MessageMapping("/chattings/{chattingRoomId}/messages")
    public void chat(@DestinationVariable Long chattingRoomId, ChattingRequest chattingRequest) {
        simpMessagingTemplate.convertAndSend("/subscription/chattings/" + chattingRoomId, chattingRequest.getContent());
        chattingService.save(chattingRoomId, chattingRequest);
    }
}
