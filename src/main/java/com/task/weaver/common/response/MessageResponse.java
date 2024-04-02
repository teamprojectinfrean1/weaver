package com.task.weaver.common.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 반환하는 데이터 없이, 상태코드와 메시지만 보내는 응답 형식
 */
@Getter
public class MessageResponse extends DefaultResponse{

    private final Boolean isSuccess;

    private MessageResponse(HttpStatus status, String message, Boolean isSuccess) {
        super(status.value(), message);
        this.isSuccess = isSuccess;
    }

    public static MessageResponse of(HttpStatus status, String message, Boolean isSuccess) {
        return new MessageResponse(status, message, isSuccess);
    }
}
