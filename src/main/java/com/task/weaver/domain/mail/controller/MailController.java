package com.task.weaver.domain.mail.controller;

import com.task.weaver.common.response.DataResponse;
import com.task.weaver.common.response.MessageResponse;
import com.task.weaver.domain.mail.dto.EmailCheckDto;
import com.task.weaver.domain.mail.dto.EmailRequestDto;
import com.task.weaver.domain.mail.service.MailSendService;
import com.task.weaver.domain.user.dto.request.RequestUpdatePassword;
import com.task.weaver.domain.user.dto.response.ResponseUserIdNickname;
import com.task.weaver.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/emails")
@RestController
@RequiredArgsConstructor
@Log4j2
public class MailController {
    private final MailSendService mailService;
    private final UserService userService;

    @PostMapping("/findId/verification/request")
    public ResponseEntity<MessageResponse> mailSendForId(@RequestBody @Valid EmailRequestDto emailDto) {
        log.info("Find ID, verify email: {}", emailDto.getEmail());
        return ResponseEntity.ok(MessageResponse.of(HttpStatus.OK, mailService.sendVerificationEmail(emailDto.getEmail())));
    }

    @PostMapping("/findId/verification/check")
    public ResponseEntity<DataResponse<ResponseUserIdNickname>> AuthCheckForId(
            @RequestBody @Valid EmailCheckDto emailCheckDto) {
        Boolean checked = mailService.CheckAuthNum(emailCheckDto.getEmail(), emailCheckDto.getVerificationCode());
        ResponseUserIdNickname targetUser = userService.getUser(emailCheckDto.getEmail(), checked);
        return new ResponseEntity<>(DataResponse.of(HttpStatus.OK, "해당 유저를 반환합니다.", targetUser), HttpStatus.OK);
    }

    @PostMapping("/findPassword/verification/request")
    public ResponseEntity<MessageResponse> mailSendForPassword(@RequestBody @Valid EmailRequestDto emailDto) {
        log.info("비밀번호 찾기 이메일 인증 :" + emailDto.getEmail());
        return new ResponseEntity<>(MessageResponse.of(HttpStatus.OK, mailService.joinEmail(emailDto.getEmail())),
                HttpStatus.OK);
    }

    @PostMapping("/findPassword/verification/check")
    public ResponseEntity<MessageResponse> AuthCheckForPassword(@RequestBody @Valid EmailCheckDto emailCheckDto) {
        Boolean checked = mailService.CheckAuthNum(emailCheckDto.getEmail(), emailCheckDto.getVerificationCode());
        return new ResponseEntity<>(MessageResponse.of(HttpStatus.OK, "verificationCode 인증 성공"), HttpStatus.ACCEPTED);
    }

    @PutMapping("/findPassword/verification/update")
    public ResponseEntity<MessageResponse> AuthPasswordUpdate(@RequestBody @Valid RequestUpdatePassword requestUpdatePassword) {
        userService.updateUser(requestUpdatePassword);
        return new ResponseEntity<>(MessageResponse.of(HttpStatus.OK, "비밀번호 변경 성공"), HttpStatus.ACCEPTED);
    }
}