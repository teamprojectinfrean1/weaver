package com.task.weaver.common.exception.comment;

import com.task.weaver.common.exception.BusinessException;
import com.task.weaver.common.exception.ErrorCode;

public class CommentNotFoundException extends BusinessException {
    public CommentNotFoundException(Throwable cause) {
        super(ErrorCode.CHATTINGROOM_NOT_FOUND, cause);
    }
}
