package com.task.weaver.common.notification;

import com.task.weaver.common.notification.entity.NotificationEvent;
import com.task.weaver.common.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;


/**
 * @EventListener로 이벤트를 구독할 수 있으며,
 * 발행한 이벤트 타입(NotificationEvent)을 인자로 받는 리스너가 이벤트를 처리한다.
 * 여기서는 알림을 받아야하는 펀딩한 유저가 다수이기 때문에 빠르게 발송하기 위해 비동기처리 하였다.  (Thread pool 설정 필요)
 */
@RequiredArgsConstructor
@Component
public class NotificationEventHandler {

    private final NotificationService notificationService;

    @Async
    @EventListener
    public void handleEvent(NotificationEvent event) {
        notificationService.sendNotification(event);
    }
}
