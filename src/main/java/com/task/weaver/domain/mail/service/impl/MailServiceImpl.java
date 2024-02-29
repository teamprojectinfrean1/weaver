package com.task.weaver.domain.mail.service.impl;

import com.task.weaver.common.exception.ErrorCode;
import com.task.weaver.common.exception.user.UnableSendMailException;
import com.task.weaver.domain.mail.service.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {
    private final JavaMailSender emailSender;

    @Override
    public void sendMail(final String toEmail, final String title, final String text) throws RuntimeException {

        SimpleMailMessage emailForm = createEmailForm(toEmail, title, text);
        try {
            emailSender.send(emailForm);
        } catch (RuntimeException e) {
            log.debug("MailService.sendEmail exception occur toEmail: {}, " +
                    "title: {}, text: {}", toEmail, title, text);
            throw new UnableSendMailException(new Throwable(ErrorCode.UNABLE_TO_SEND_EMAIL.getMessage()));
        }
    }

    @Override
    public SimpleMailMessage createEmailForm(final String toEmail, final String title, final String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(title);
        message.setText(text);
        return message;
    }

    @Override
    public boolean isVerify(final String email) {
        return false;
    }

    @Override
    public boolean valid(final String email, final String code) {
        return false;
    }
}
