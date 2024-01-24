package com.task.weaver.common.exception;

import lombok.Getter;

@Getter
public abstract class BusinessException extends RuntimeException{

    private final ErrorCode errorCode;

    protected BusinessException(ErrorCode errorCode, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(errorCode.getMessage(), cause, enableSuppression, writableStackTrace);
        this.errorCode = errorCode;
    }

    public BusinessException(ErrorCode errorCode, Throwable cause) {
        this(errorCode, cause, true, true);
    }

    public ErrorCode getErrorCode(){
        return errorCode;
    }
}
