package com.task.weaver.common.exception.user;

import com.task.weaver.common.exception.BusinessException;
import com.task.weaver.common.exception.ErrorCode;

public class MismatchedPassword extends BusinessException {

    public MismatchedPassword(final ErrorCode errorCode, final String message) {
        super(errorCode, message);
    }
}
