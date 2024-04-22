package com.task.weaver.common.response;

import lombok.Getter;

@Getter
public abstract class DefaultResponse {

    protected int httpStatus;

    protected String message;

    protected DefaultResponse(int status, String message) {
        this.httpStatus = status;
        this.message = message;
    }
}
