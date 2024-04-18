package com.task.weaver.common.aop;

import java.time.Duration;
import java.time.LocalDateTime;
import lombok.val;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
class LoggingStopWatchAdvice {

    private static final Logger logger = LoggerFactory.getLogger(LoggingStopWatchAdvice.class);

    @Around("@annotation(com.task.weaver.common.aop.annotation.LoggingStopWatch)")
    public Object atTarget(ProceedingJoinPoint joinPoint) throws Throwable {
        LocalDateTime startAt = LocalDateTime.now();
        logger.info("Start At : {}", startAt);

        Object proceed = joinPoint.proceed();

        LocalDateTime endAt = LocalDateTime.now();

        logger.info("End At : {}", startAt);
        logger.info("Logic Duration : {}ms", Duration.between(startAt, endAt).toMillis());

        return proceed;
    }
}