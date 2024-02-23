package com.task.weaver.common.exception.user;

import com.task.weaver.common.exception.BusinessException;
import com.task.weaver.common.exception.ErrorCode;

public class UserNotFoundException extends BusinessException {

    public UserNotFoundException(Throwable cause) {
        super(ErrorCode.USER_NOT_FOUND, cause);
    }
}
