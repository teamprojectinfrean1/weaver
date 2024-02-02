package com.task.weaver.common.sse.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface SseService {
    SseEmitter subscribe(String user_id, Object data);
    void sendToClient(String username, Object data);
    SseEmitter createEmitter(HttpServletRequest request);
    void closeEmitter(String username);
}