package com.task.weaver.domain.websocket.controller;

import com.task.weaver.domain.websocket.dto.request.RequestCreateChatting;
import com.task.weaver.domain.websocket.service.ChattingService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChattingWsController {
    private final ChattingService chattingService;
    private final SimpMessagingTemplate simpMessagingTemplate;  //service에서 controller 단으로 옮김. 어디로 보내는지 더 보기 쉽게

    @MessageMapping("/{chattingRoomId}/messages")
   // @SendTo("/sub/{chattingRoomId}")                      //convertAndSend대신 이걸 사용해도 됨
    public void chat(@DestinationVariable("chattingRoomId") Long chattingRoomId, RequestCreateChatting requestCreateChatting) { //@DestinationVariable에 추가로 chattingRoomId를 괄호 쓰고 넣어줘야 파라미터 인식을 함.
        chattingService.save(chattingRoomId, requestCreateChatting);
        simpMessagingTemplate.convertAndSend("/sub/" + chattingRoomId, requestCreateChatting.getContent());
    }
}


