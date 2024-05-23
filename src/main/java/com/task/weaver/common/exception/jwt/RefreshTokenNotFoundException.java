package com.task.weaver.common.exception.jwt;

import com.task.weaver.common.exception.BusinessException;
import com.task.weaver.common.exception.ErrorCode;

public class RefreshTokenNotFoundException extends BusinessException {
    public RefreshTokenNotFoundException(final ErrorCode errorCode, final String message) {
        super(errorCode, message);
    }
}
