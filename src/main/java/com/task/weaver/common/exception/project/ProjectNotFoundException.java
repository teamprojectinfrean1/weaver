package com.task.weaver.common.exception.project;

import com.task.weaver.common.exception.BusinessException;
import com.task.weaver.common.exception.ErrorCode;

public class ProjectNotFoundException extends BusinessException {
    public ProjectNotFoundException(Throwable cause) {
        super(ErrorCode.PROJECT_NOT_FOUND, cause);
    }

    public ProjectNotFoundException(final ErrorCode errorCode, final String message) {
        super(errorCode, message);
    }
}
