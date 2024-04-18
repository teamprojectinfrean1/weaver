package com.task.weaver.common.aop;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    private final String ANNOTATION_LOGGER_TARGET = "@annotation(com.task.weaver.common.aop.annotation.Logger)";

    @Before(ANNOTATION_LOGGER_TARGET)
    public void loggingBefore(JoinPoint joinPoint) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        log.info("## REQUEST [{}] {} {}, Payload: {}", request.getRemoteAddr(), request.getMethod(), request.getRequestURI(), getPayload(joinPoint));
    }

    @AfterReturning(value = ANNOTATION_LOGGER_TARGET, returning = "result")
    public void afterReturn(Object result) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        log.info("## RESPONSE [{}] {} {}, Result: {}", request.getRemoteAddr(), request.getMethod(), request.getRequestURI(), result.toString());
    }

    @AfterThrowing(value = ANNOTATION_LOGGER_TARGET, throwing = "e")
    public void loggingAfterThrowing(Exception e) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        log.info("## RESPONSE [{}] {} {}, Exception: {}", request.getRemoteAddr(), request.getMethod(), request.getRequestURI(), e.toString());
    }

    private String getPayload(JoinPoint joinPoint) {
        return Arrays.stream(joinPoint.getArgs())
                .map(Object::toString)
                .collect(Collectors.joining(", "));
    }
}
