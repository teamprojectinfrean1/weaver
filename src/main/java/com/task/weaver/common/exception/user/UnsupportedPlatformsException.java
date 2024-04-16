package com.task.weaver.common.exception.user;

import com.task.weaver.common.exception.BusinessException;
import com.task.weaver.common.exception.ErrorCode;

public class UnsupportedPlatformsException extends BusinessException {
    public UnsupportedPlatformsException(final ErrorCode errorCode, final String message) {
        super(errorCode, message);
    }
}
