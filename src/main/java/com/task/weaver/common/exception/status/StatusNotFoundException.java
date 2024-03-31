package com.task.weaver.common.exception.status;

import com.task.weaver.common.exception.BusinessException;
import com.task.weaver.common.exception.ErrorCode;

public class StatusNotFoundException extends BusinessException {

    public StatusNotFoundException(final ErrorCode errorCode, final String message) {
        super(errorCode, message);
    }
}
