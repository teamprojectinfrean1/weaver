package com.task.weaver.common.response;

import lombok.Getter;

@Getter
public abstract class DefaultResponse {

    protected int status;

    protected String message;

    protected DefaultResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
