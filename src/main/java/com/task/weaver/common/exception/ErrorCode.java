package com.task.weaver.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    // Common
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "C001", "값이 올바르지 않습니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "C002", "지원하지 않는 Http Method 입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "C003", "서버 에러"),
    INVALID_TYPE_VALUE(HttpStatus.BAD_REQUEST, "C004", "입력 값의 타입이 올바르지 않습니다."),
    HANDLE_ACCESS_DENIED(HttpStatus.FORBIDDEN, "C005", "접근이 거부 되었습니다."),
    RESOURCE_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "R001", "해당 리소스에 대한 권한이 없습니다."),

    // Project
    PROJECT_NOT_FOUND(HttpStatus.NOT_FOUND, "P001", "해당 프로젝트를 찾을 수 없습니다."),

    // User
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "U001", "해당 유저를 찾을 수 없습니다."),
    EMAIL_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "U001", "이미 존재하는 이메일입니다."),
    USER_EMAIL_NOT_FOUND(HttpStatus.NOT_FOUND, "U002", "사용자를 찾을 수 없습니다."),

    // Authorization
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "A001", "패스워드가 일치하지 않습니다."),

    //Task
    TASK_NOT_FOUND(HttpStatus.NOT_FOUND, "T001", "해당 task를 찾을 수 업습니다."),
    // Chatting Room
    CHATTINGROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "CHR001", "해당 채팅룸을 찾을 수 없습니다."),

    //Chatting
    CHATTING_NOT_FOUND(HttpStatus.NOT_FOUND, "CH001", "해당 채팅을 찾을 수 없습니다."),

    // Email
    UNABLE_TO_SEND_EMAIL(HttpStatus.BAD_REQUEST, "M001", "메일을 보낼 수 없습니다."),
    NO_SEARCH_EMAIL(HttpStatus.BAD_REQUEST, "M002", "존재하지 않은 메일입니다."),
    NO_SEARCH_ALGORITHM(HttpStatus.FORBIDDEN, "M003", "검색 알고리즘이 없습니다."),
    NO_MATCHED_VERIFICATION_CODE(HttpStatus.FORBIDDEN, "M004", "인증 코드가 일치하지않습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public int getStatus() {
        return this.status.value();
    }
}
