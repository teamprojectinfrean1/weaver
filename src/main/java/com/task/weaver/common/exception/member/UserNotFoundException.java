package com.task.weaver.common.exception.member;

import com.task.weaver.common.exception.BusinessException;
import com.task.weaver.common.exception.ErrorCode;

public class UserNotFoundException extends BusinessException {

    public UserNotFoundException(Throwable cause) {
        super(ErrorCode.USER_NOT_FOUND, cause);
    }

    public UserNotFoundException(final ErrorCode errorCode, final String message) {
        super(errorCode, message);
    }
}
