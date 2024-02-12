package com.task.weaver.domain.security.mail;

public interface MailService {

    // 인증 메일을 보냅니다.
    void send (String email, String code) throws RuntimeException;

    // 이메일 인증 여부를 확인합니다.
    boolean isVerify (String email);

    // 이메일 인증합니다
    boolean valid (String email, String code);

}
