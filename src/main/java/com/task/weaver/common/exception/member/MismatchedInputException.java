package com.task.weaver.common.exception.member;

import com.task.weaver.common.exception.BusinessException;
import com.task.weaver.common.exception.ErrorCode;

public class MismatchedInputException extends BusinessException {
    public MismatchedInputException(final ErrorCode errorCode, final String message) {
        super(errorCode, message);
    }
}
