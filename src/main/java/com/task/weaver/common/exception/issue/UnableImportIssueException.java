package com.task.weaver.common.exception.issue;

import com.task.weaver.common.exception.BusinessException;
import com.task.weaver.common.exception.ErrorCode;

public class UnableImportIssueException extends BusinessException {
    public UnableImportIssueException(final ErrorCode errorCode, final String message) {
        super(errorCode, message);
    }
}
