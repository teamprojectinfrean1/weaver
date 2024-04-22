package com.task.weaver.common.exception.notification;

import com.task.weaver.common.exception.BusinessException;
import com.task.weaver.common.exception.ErrorCode;

public class NotificationException extends BusinessException {
    public NotificationException(final ErrorCode errorCode, final String message) {
        super(errorCode, message);
    }
}
