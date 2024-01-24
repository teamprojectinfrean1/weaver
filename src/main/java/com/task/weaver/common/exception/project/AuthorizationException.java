package com.task.weaver.common.exception.project;

import com.task.weaver.common.exception.BusinessException;
import com.task.weaver.common.exception.ErrorCode;

public class AuthorizationException extends BusinessException {
    protected AuthorizationException(final ErrorCode errorCode, final Throwable cause) {
        super(errorCode, cause);
    }
}
