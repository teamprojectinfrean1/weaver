package com.task.weaver.common.notification;

import com.task.weaver.common.notification.entity.NotificationEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * 이벤트 발행 객체
 */
@RequiredArgsConstructor
@Component
public class NotificationEventPublisher {

    private final ApplicationEventPublisher eventPublisher;

    public void publishEvent(NotificationEvent event) {
        eventPublisher.publishEvent(event);
    }
}
