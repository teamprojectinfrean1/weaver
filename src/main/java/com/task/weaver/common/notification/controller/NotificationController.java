package com.task.weaver.common.notification.controller;

import static com.task.weaver.common.exception.ErrorCode.*;

import com.task.weaver.common.exception.notification.NotificationException;
import com.task.weaver.common.notification.service.NotificationService;
import java.time.Duration;
import java.time.LocalTime;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notification")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping(value = "/connect", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> SseConnect(@AuthenticationPrincipal UserDetails userDetails) {
        if (ObjectUtils.isEmpty(userDetails)) {
            throw new NotificationException(INVALID_INPUT_VALUE, INVALID_INPUT_VALUE.getMessage());
        }
        return ResponseEntity.ok(notificationService.subscribe(userDetails.getUsername()));
    }

    @GetMapping(value = "/subscribe/{uuid}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(@PathVariable String uuid) {
        return notificationService.subscribe(uuid);
    }

    @PostMapping("/send-data/{uuid}")
    public void sendData(@PathVariable String uuid) {
        notificationService.notify(uuid, "data");
    }

    @GetMapping(path = "/stream-flux", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> streamFlux() {
        return Flux.interval(Duration.ofSeconds(1)).map(sequence -> ServerSentEvent.<String> builder()
                .id(String.valueOf(sequence))
                .event("periodic-event")
                .data("SSE - " + LocalTime.now().toString())
                .build());
    }
}