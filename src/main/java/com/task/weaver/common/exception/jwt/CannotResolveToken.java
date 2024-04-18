package com.task.weaver.common.exception.jwt;

import com.task.weaver.common.exception.BusinessException;
import com.task.weaver.common.exception.ErrorCode;

public class CannotResolveToken extends BusinessException {
    public CannotResolveToken(final ErrorCode errorCode, final String message) {
        super(errorCode, message);
    }
}
