package com.task.weaver.domain.mail.service;

import org.springframework.mail.SimpleMailMessage;

public interface MailService {

    void sendMail (String toEmail,
                   String title,
                   String text) throws RuntimeException;

    SimpleMailMessage createEmailForm(String toEmail,
                                              String title,
                                              String text);

    // 이메일 인증 여부를 확인합니다.
    boolean isVerify (String email);

    // 이메일 인증합니다
    boolean valid (String email, String code);
}
