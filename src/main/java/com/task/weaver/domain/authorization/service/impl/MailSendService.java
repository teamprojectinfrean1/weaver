package com.task.weaver.domain.authorization.service.impl;

import com.task.weaver.common.exception.user.UnableSendMailException;
import com.task.weaver.domain.authorization.dto.response.EmailCode;
import com.task.weaver.domain.authorization.redis.RedisEmailUtil;
import com.task.weaver.domain.member.user.dto.response.ResponseGetUser;
import com.task.weaver.domain.member.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Random;

@RequiredArgsConstructor
@Service
public class MailSendService {

    private final JavaMailSender mailSender;
    private final RedisEmailUtil redisEmailUtil;
    private final UserService userService;

    @Value("${spring.mail.auth-code-expiration-millis}")
    private String authCodeExpirationMillis;
    public int authNumber;

    public EmailCode sendVerificationEmail(final String email) {
        ResponseGetUser user = userService.getUserByMail(email);
        return joinEmail(user.getEmail());
    }

    public boolean CheckAuthNum(String email, String authNum) {
        if (redisEmailUtil.getData(authNum) == null) {
            return false;
        }
        return redisEmailUtil.getData(authNum).equals(email);
    }

    public EmailCode joinEmail(String email) {
        makeRandomNumber();
        String setFrom = "jcjk0302@likelion.org";
        String title = "Weaver 회원 가입 인증 이메일 입니다.";
        String content =
                "Task Gram을 방문해주셔서 감사합니다." +
                        "<br><br>" +
                        "인증 번호는 " + authNumber + "입니다." +
                        "<br>" +
                        "웹 사이트에 인증번호를 입력해주세요";
        mailSend(setFrom, email, title, content);
        return EmailCode.builder().isSuccess(true).build();
    }

    public void mailSend(String setFrom, String toMail, String title, String content) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
            helper.setFrom(setFrom);
            helper.setTo(toMail);
            helper.setSubject(title);
            helper.setText(content, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
            throw new UnableSendMailException(e, true);
        }
        redisEmailUtil.setDataExpire(Integer.toString(authNumber), toMail, Long.parseLong(authCodeExpirationMillis));
    }

    private void makeRandomNumber() {
        Random r = new Random();
        StringBuilder randomNumber = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            randomNumber.append(r.nextInt(10));
        }

        authNumber = Integer.parseInt(randomNumber.toString());
    }
}
