package com.task.weaver.common.sse.service.Impl;

import com.task.weaver.common.sse.repository.SseRepository;
import com.task.weaver.common.sse.service.SseService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
@Service
@RequiredArgsConstructor
public class SseServiceImpl implements SseService {
    private static final Long DEFAULT_TIMEOUT = 120L * 1000 * 60;
    private final SseRepository sseRepository;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final Logger log = LoggerFactory.getLogger(SseServiceImpl.class);
    @Override
    public SseEmitter subscribe(String username, Object data) { //username을 key값으로, emitter를 찾아낸 후 그 user에게 data를 보낸다.

        SseEmitter emitter = sseRepository.get(username);
        sendToClient(username,data);

        return emitter;
    }
    @Override
    public void sendToClient(String username, Object data) {
        SseEmitter emitter = sseRepository.get(username);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event()     //이 부분이 핵심.
                        .id(username)
                        .name("notice")  //프론트에서 eventsource.addEventListener("sse" ...) 이 부분
                        .data(data));              //전송할 데이터

            } catch (IOException exception) {
                sseRepository.deleteById(username);
                emitter.completeWithError(exception);
            }
        }
        else if(emitter == null){
            log.info("sseEmitter is null");
        }
    }
    @Override
    public SseEmitter createEmitter(HttpServletRequest request) {  //emitter 생성. user가 실시간 알림을 받아야할 시점에 호출되는 메서드. (ex. 로그인 직후)

        String username = "";    //이 부분은 나중에 결정해야함. 무엇을 key값으로 해야할까?
        SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);

        //더미 데이터 send
        String dummy = "connect request";
        sseRepository.save(username, emitter);    //emitter를 생성해 저장
        try {
            emitter.send(SseEmitter.event()
                    .id(username)
                    .name("notice")
                    .data(dummy));                //emitter생성 후, 더미 데이터를 전송해야한다. 안 그러면 오류가 발생.

            log.info("data 전송 완료 : message ==> " + dummy);
        } catch (IOException exception) {
            sseRepository.deleteById(username);
            emitter.completeWithError(exception);
        }

        // Emitter가 완료될 때(모든 데이터가 성공적으로 전송된 상태) Emitter를 삭제한다.한번 보내고 말것이 아니라 주석처리 하였음.
        emitter.onCompletion(() -> sseRepository.deleteById(username));

        // Emitter가 타임아웃 되었을 때(지정된 시간동안 어떠한 이벤트도 전송되지 않았을 때) Emitter를 삭제한다.
        emitter.onTimeout(() -> sseRepository.deleteById(username));
        emitter.onError((e) -> sseRepository.deleteById(username));
        return emitter;
    }

    //로그아웃 시 호출
    @Override
    public void closeEmitter(String username){
        sseRepository.deleteById(username);
    }
}
