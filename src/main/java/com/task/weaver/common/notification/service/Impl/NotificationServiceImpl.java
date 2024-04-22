package com.task.weaver.common.notification.service.Impl;

import com.task.weaver.common.notification.entity.NotificationEvent;
import com.task.weaver.common.notification.repository.NotificationRepository;
import com.task.weaver.common.notification.service.NotificationService;
import com.task.weaver.domain.member.entity.Member;
import com.task.weaver.domain.member.service.MemberService;
import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;
    private final NotificationRepository notificationRepository;
    private final MemberService memberService;
    private final Logger log = LoggerFactory.getLogger(NotificationServiceImpl.class);

    /**
     * 클라이언트가 구독을 위해 호출하는 메서드.
     *
     * @param uuid - 구독하는 클라이언트의 사용자 아이디.
     * @return SseEmitter - 서버에서 보낸 이벤트 Emitter
     */
    @Override
    public SseEmitter subscribe(String uuid) {
        return createEmitter(uuid);
    }

    /**
     * 서버의 이벤트를 클라이언트에게 보내는 메서드 다른 서비스 로직에서 이 메서드를 사용해 데이터를 Object event에 넣고 전송하면 된다.
     *
     * @param uuid  - 메세지를 전송할 사용자의 아이디.
     * @param event - 전송할 이벤트 객체.
     */
    @Override
    public void notify(String uuid, Object event) {
        sendToClient(uuid, event);
    }

    /**
     * 클라이언트에게 데이터를 전송
     *
     * @param uuid - 데이터를 받을 사용자의 아이디.
     * @param data - 전송할 데이터.
     */
    private void sendToClient(String uuid, Object data) {
        SseEmitter emitter = notificationRepository.get(uuid);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event().id(uuid).name("sse").data(data));
            } catch (IOException exception) {
                notificationRepository.deleteById(uuid);
                emitter.completeWithError(exception);
            }
        }
    }

    /**
     * 사용자 아이디를 기반으로 이벤트 Emitter를 생성
     *
     * @param uuid - 사용자 아이디.
     * @return SseEmitter - 생성된 이벤트 Emitter.
     */
    private SseEmitter createEmitter(String uuid) {
        SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);
        notificationRepository.save(uuid, emitter);

        emitter.onCompletion(() -> notificationRepository.deleteById(uuid));
        emitter.onTimeout(() -> notificationRepository.deleteById(uuid));
        emitter.onError((e) -> emitter.complete());
        send("MsgFormat.SUBSCRIBE", uuid, emitter);
        return emitter;
    }

    private void send(Object data, String emitterKey, SseEmitter sseEmitter) {
        try {
            log.info("send to client {}:[{}]", emitterKey, data);
            // 이벤트 데이터 전송
            sseEmitter.send(SseEmitter.event()
                    .id(emitterKey)
                    .data(data, MediaType.APPLICATION_JSON)); // data가 메시지만 포함된다면 타입을 지정해줄 필요는 없다.
        } catch (IOException | IllegalStateException e) {
            log.error("IOException | IllegalStateException is occurred. ", e);
            notificationRepository.deleteById(emitterKey);
        }
    }

    @Override
    public void closeEmitter(String username) {
        notificationRepository.deleteById(username);
    }

    @Override
    public void sendNotification(final NotificationEvent event) {

    }

//    @Transactional
//    @Override
//    public void sendNotification(final NotificationEvent event) {
//        String key = event.memberKey();
//        Member member = memberService.getMemberOrThrow(key);
//        Notification notification =
//                Notification.of(event.message(), event.notificationType(), event.relatedUri());
//        notification.addMember(member);
//        notificationRepository.save(notification);
//
//        // 유저와 연결된 emitter를 찾는다.
//        sseEmitterRepository.findById(key)
//                .ifPresent(emitter ->
//                        send(NotificationDto.fromEntity(notification), key, emitter)); // 데이터 전송
//    }
}
