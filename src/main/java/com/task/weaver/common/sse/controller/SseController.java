package com.task.weaver.common.sse.controller;

import com.task.weaver.common.sse.service.SseService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/sse")
public class SseController {

    private final SseService sseService;

    //로그인성공시 반드시 sseConnect를 호출해야 합니다.
    @GetMapping(value = "/connect", produces = MediaType.TEXT_EVENT_STREAM_VALUE)  //produces = MediaType.TEXT_EVENT_STREAM_VALUE
    public SseEmitter SseConnect(HttpServletRequest request){
        SseEmitter sse = sseService.createEmitter(request);
        return sse;
    }


//    // id 에게 알림메세지를 보낸다.
//    @GetMapping(value = "/apply") //반드시 json으로 반환 produces = MediaType.APPLICATION_JSON_VALUE//
//    public void subscribe(@RequestBody RequestApplicationDto requestApplicationDto) {
////        Long board_id_long = Long.parseLong(id);
//
//        ResponseSseDto responseSseDto = applicationService.ApplicationToSseResponse(requestApplicationDto.getBoard_id(), requestApplicationDto.getRole_id());
//
//        String message = responseSseDto.getTitle()+" 의 "+ responseSseDto.getRole() +" 에 신청이 1건 있습니다.";
//        SseEmitter sseEmitter = sseService.subscribe(requestApplicationDto.getAuthorName(), message);
//    };
}