package com.task.weaver.common.exception.task;

import com.task.weaver.common.exception.BusinessException;
import com.task.weaver.common.exception.ErrorCode;

public class TaskNotFoundException extends BusinessException {
    public TaskNotFoundException(Throwable cause) {
        super(ErrorCode.TASK_NOT_FOUND, cause);
    }
}
