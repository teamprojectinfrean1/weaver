package com.task.weaver.common.notification.repository;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
@RequiredArgsConstructor
public class NotificationRepository {
    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    public SseEmitter save(String uuid, SseEmitter emitter) {
        emitters.put(uuid, emitter);
        return emitter;
    }

    public Optional<SseEmitter> findById(String memberId) {
        return Optional.ofNullable(emitters.get(memberId));
    }

    public void deleteById(String eventId) {
        emitters.remove(eventId);
    }

    public SseEmitter get(String uuid) {
        return emitters.get(uuid);
    }
}