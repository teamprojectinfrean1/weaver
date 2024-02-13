package com.task.weaver.common.sse.repository;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
@RequiredArgsConstructor
public class SseRepository {
    // 모든 Emitters를 저장하는 ConcurrentHashMap
    // redis pub/sub 적용하려면 변경해야함. 여러 인스턴스를 운용할 때는 사용하면 안 되는 구조.
    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    public void save(String username, SseEmitter emitter) {
        emitters.put(username, emitter);
    }
    //(id , emitter<time_out>)

    public void deleteById(String username) {
        emitters.remove(username);
    }

    public SseEmitter get(String username) {
        return emitters.get(username);
    }
}