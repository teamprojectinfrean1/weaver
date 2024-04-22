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
    UNSUPPORTED_OAUTH(HttpStatus.NOT_ACCEPTABLE, "C006", "지원하지않는 플랫폼입니다."),
    REFRESH_JWT_EXPIRED(HttpStatus.UNAUTHORIZED, "J003", "만료된 리프레시 토큰입니다."),
    REFRESH_TOKEN_RESOLVE(HttpStatus.BAD_REQUEST, "J004", "잘못된 리프레시 토큰 형식입니다."),
    MISSING_REQUEST_PARAMETER(HttpStatus.BAD_REQUEST, "C007", "요청 파라미터를 찾을 수 없습니다."),

    // Project
    PROJECT_NOT_FOUND(HttpStatus.NOT_FOUND, "P001", "해당 프로젝트를 찾을 수 없습니다."),

    // UserOauthMember
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "U001", "해당 유저를 찾을 수 없습니다."),
    EMAIL_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "U001", "이미 존재하는 이메일입니다."),
    USER_EMAIL_NOT_FOUND(HttpStatus.NOT_FOUND, "U002", "사용자를 찾을 수 없습니다."),
    MISMATCHED_PASSWORD(HttpStatus.NOT_MODIFIED, "U003", "입력 값이 일치하지않습니다."),
    EMPTY_FILE(HttpStatus.BAD_REQUEST, "U004", "파일 항목이 비어있습니다."),
    CANNOT_SAVED_FILE(HttpStatus.BAD_REQUEST, "U005", "파일 저장에 실패했습니다."),
    STORE_FILE_OUTSIDE_CURRENT_DIRECTORY_MESSAGE(HttpStatus.BAD_REQUEST, "U006", "유효하지 않은 저장 경로입니다."),
    MEMBER_CANNOT_CONVERTED(HttpStatus.BAD_REQUEST, "U007", "해당 객체를 변환할 수 없습니다."),
    MISMATCHED_MEMBER(HttpStatus.NOT_ACCEPTABLE, "U008", "입력 유저가 일치하지 않습니다."),
    MISMATCHED_INPUT_VALUE(HttpStatus.NOT_ACCEPTABLE, "U009", "올바르지 않은 입력값 입니다."),

    // Authorization
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "A001", "패스워드가 일치하지 않습니다."),

    // Task
    TASK_NOT_FOUND(HttpStatus.NOT_FOUND, "T001", "해당 task를 찾을 수 업습니다."),

    // Chatting Room
    CHATTINGROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "CHR001", "해당 채팅룸을 찾을 수 없습니다."),

    // Chatting
    CHATTING_NOT_FOUND(HttpStatus.NOT_FOUND, "CH001", "해당 채팅을 찾을 수 없습니다."),

    // Email
    UNABLE_TO_SEND_EMAIL(HttpStatus.BAD_REQUEST, "M001", "메일을 보낼 수 없습니다."),
    NO_SEARCH_EMAIL(HttpStatus.BAD_REQUEST, "M002", "존재하지 않은 메일입니다."),
    NO_SEARCH_ALGORITHM(HttpStatus.FORBIDDEN, "M003", "검색 알고리즘이 없습니다."),
    NO_MATCHED_VERIFICATION_CODE(HttpStatus.FORBIDDEN, "M004", "인증 코드가 일치하지않습니다."),

    // Status
    INVALID_STATUS(HttpStatus.BAD_REQUEST, "S001", "유효하지않은 STATUS입니다."),

    // Comment
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "CM001", "해당 코멘트를 찾을 수 없습니다"),

    // Issue
    ISSUE_NOT_FOUND(HttpStatus.NOT_FOUND, "I001", "해당 이슈를 찾을 수 없습니다.");

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
