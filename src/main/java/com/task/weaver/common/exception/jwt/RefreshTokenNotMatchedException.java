package com.task.weaver.common.exception.jwt;

import com.task.weaver.common.exception.BusinessException;
import com.task.weaver.common.exception.ErrorCode;

public class RefreshTokenNotMatchedException extends BusinessException {
    public RefreshTokenNotMatchedException(final ErrorCode errorCode, final String message) {
        super(errorCode, message);
    }
}
