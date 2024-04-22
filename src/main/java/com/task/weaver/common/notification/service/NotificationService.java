package com.task.weaver.common.notification.service;

import com.task.weaver.common.notification.entity.NotificationEvent;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface NotificationService {
    SseEmitter subscribe(String memberUuid);
    void notify(String uuid, Object event);
    void closeEmitter(String username);

    void sendNotification(NotificationEvent event);
}