package com.task.weaver.domain.userOauthMember.user.controller;

import static com.task.weaver.domain.member.service.impl.MemberServiceImpl.setCookieAndHeader;

import com.task.weaver.common.aop.annotation.Logger;
import com.task.weaver.common.response.DataResponse;
import com.task.weaver.common.response.MessageResponse;
import com.task.weaver.domain.member.dto.request.EmailCheckDto;
import com.task.weaver.domain.member.dto.request.EmailRequest;
import com.task.weaver.domain.member.dto.request.RequestSignIn;
import com.task.weaver.domain.member.dto.response.EmailCode;
import com.task.weaver.domain.member.dto.response.ResponseToken;
import com.task.weaver.domain.member.service.MemberService;
import com.task.weaver.domain.member.service.impl.MailSendService;
import com.task.weaver.domain.userOauthMember.user.dto.request.RequestCreateUser;
import com.task.weaver.domain.userOauthMember.user.dto.request.RequestUpdatePassword;
import com.task.weaver.domain.userOauthMember.user.dto.request.RequestUpdateUser;
import com.task.weaver.domain.userOauthMember.user.dto.response.ResponseGetMember;
import com.task.weaver.domain.userOauthMember.user.dto.response.ResponseSimpleURL;
import com.task.weaver.domain.userOauthMember.user.dto.response.ResponseUserIdNickname;
import com.task.weaver.domain.userOauthMember.user.dto.response.ResponseUuid;
import com.task.weaver.domain.userOauthMember.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "User Controller", description = "사용자 관련 컨트롤러")
public class UserController {

    private final UserService userService;
    private final MemberService memberService;
    private final MailSendService mailService;

    @Operation(summary = "회원가입", description = "사용자가 회원가입")
    @PostMapping(value = "/join", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<DataResponse<ResponseGetMember>> addUser(@RequestPart(value = "requestCreateUser") RequestCreateUser requestCreateUser,
                                                                   @RequestPart(value = "multipartFile", required = false) MultipartFile multipartFile)
                                                                    throws IOException {

        log.info("controller - join - before");
        ResponseGetMember responseGetMember = userService.addUser(requestCreateUser, multipartFile);
        log.info("controller - join - after");
        return ResponseEntity.status(HttpStatus.OK)
                .body(DataResponse.of(HttpStatus.OK, "회원 가입 성공", responseGetMember, true));
    }

    @Logger
    @Operation(summary = "로그인", description = "로그인")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody RequestSignIn requestSignIn) {
        ResponseToken responseToken = userService.weaverLogin(requestSignIn);
        log.info("accessToken = {}", responseToken.accessToken());
        log.info("refreshToken : " + responseToken.refreshToken());
        HttpHeaders headers = setCookieAndHeader(responseToken);
        return new ResponseEntity<>(DataResponse.of(HttpStatus.CREATED, "Weaver login successfully", headers, true),
                HttpStatus.CREATED);
    }

    @Logger
    @Operation(summary = "사용자 정보 수정", description = "사용자의 정보 (닉네임, 비밀번호, 이메일) 업데이트")
    @PutMapping("/update")
    public ResponseEntity<DataResponse<ResponseGetMember>> updateUser(@RequestParam("uuid") UUID uuid,
                                                                      @RequestBody RequestUpdateUser requestUpdateUser)
            throws ParseException, IOException {

        ResponseGetMember responseGetMember = userService.updateUser(uuid, requestUpdateUser);
        return ResponseEntity.ok(DataResponse.of(HttpStatus.OK, "유저 정보 수정 성공", responseGetMember, true));
    }

    @Logger
    @Operation(summary = "사용자 정보 수정", description = "사용자의 프로필 이미지 업데이트")
    @PutMapping(value = "/update/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<DataResponse<ResponseSimpleURL>> updateProfileImage(@RequestParam("uuid") UUID uuid,
                                                                              @RequestPart(value = "multipartFile") MultipartFile multipartFile) throws IOException {

        ResponseSimpleURL responseGetMember = userService.updateProfile(multipartFile, uuid);
        return ResponseEntity.ok(DataResponse.of(HttpStatus.OK, "멤버 프로필 이미지 업데이트 성공", responseGetMember, true));
    }

    @Logger
    @Operation(summary = "사용자 삭제", description = "사용자 정보 삭제, 사용자는 사용 불가")
    @DeleteMapping()
    public ResponseEntity<String> deleteUser(@RequestParam("userId") UUID userId) {
        userService.deleteUser(userId);
        return ResponseEntity.status(HttpStatus.OK).body("user deleted");
    }

    @Logger
    @Operation(summary = "이메일 중복 체크", description = "이메일 중복을 체크")
    @GetMapping("/checkMail")
    public ResponseEntity<DataResponse<Boolean>> checkMail(@RequestParam("email") String email) {
        return new ResponseEntity<>(
                DataResponse.of(HttpStatus.OK, "중복 체크 동작", memberService.checkMail(email), true), HttpStatus.OK);
    }

    @Logger
    @Operation(summary = "아이디 중복 체크", description = "아이디 중복을 체크")
    @GetMapping("/checkId")
    public ResponseEntity<DataResponse<Boolean>> checkId(@RequestParam("id") String id) {
        return new ResponseEntity<>(DataResponse.of(HttpStatus.OK, "중복 체크 동작", memberService.checkId(id), true),
                HttpStatus.OK);
    }

    @Operation(summary = "닉네임 중복 체크", description = "닉네임 중복을 체크")
    @GetMapping("/checkNickname")
    public ResponseEntity<DataResponse<Boolean>> checkNickname(@RequestParam("nickname") String nickname) {
        return new ResponseEntity<>(
                DataResponse.of(HttpStatus.OK, "중복 체크 동작", memberService.checkNickname(nickname), true),
                HttpStatus.OK);
    }

    @Logger
    @Operation(summary = "이메일 전송", description = "랜덤 번호를 담은 이메일 전송")
    @PostMapping("/findId/verification/request")
    public ResponseEntity<DataResponse<EmailCode>> mailSendForId(@RequestBody @Valid EmailRequest emailDto) {
        log.info("Find ID, verify email: {}", emailDto.email());
        return ResponseEntity.ok(
                DataResponse.of(HttpStatus.OK, "인증 코드 전송 성공", mailService.joinEmail(emailDto.email()), true));
    }

    @Logger
    @Operation(summary = "인증 번호 확인", description = "서버에 저장된 랜덤 번호와 사용자 입력 번호 검증")
    @PostMapping("/findId/verification/check")
    public ResponseEntity<DataResponse<ResponseUserIdNickname>> AuthCheckForId(
            @RequestBody @Valid EmailCheckDto emailCheckDto) {
        Boolean checked = mailService.CheckAuthNum(emailCheckDto.email(), emailCheckDto.verificationCode());
        ResponseUserIdNickname targetUser = memberService.getMember(emailCheckDto.email(), checked);
        return ResponseEntity.ok(DataResponse.of(HttpStatus.OK, "해당 유저를 반환합니다.", targetUser, true));
    }

    @Logger
    @Operation(summary = "이메일 전송", description = "랜덤 번호를 담은 이메일 전송")
    @PostMapping("/findPassword/verification/request")
    public ResponseEntity<DataResponse<EmailCode>> mailSendForPassword(@RequestBody @Valid EmailRequest emailDto) {
        log.info("비밀번호 찾기 이메일 인증 :" + emailDto.email());
        return ResponseEntity.ok(
                DataResponse.of(HttpStatus.OK, "인증 코드 전송 성공", mailService.joinEmail(emailDto.email()), true));
    }

    @Logger
    @Operation(summary = "인증 번호 확인", description = "서버에 저장된 랜덤 번호와 사용자 입력 번호 검증")
    @PostMapping("/findPassword/verification/check")
    public ResponseEntity<DataResponse<ResponseUuid>> AuthCheckForPassword(
            @RequestBody @Valid EmailCheckDto emailCheckDto) {
        Boolean checked = mailService.CheckAuthNum(emailCheckDto.email(), emailCheckDto.verificationCode());
        ResponseUuid targetUser = memberService.getUuid(emailCheckDto.email(), checked);
        return ResponseEntity.ok(DataResponse.of(HttpStatus.OK, "verificationCode 인증 성공", targetUser, true));
    }

    @Logger
    @Operation(summary = "비밀번호 재설정", description = "인증 확인된 사용자 비밀번호 재설정")
    @PutMapping("/findPassword/verification/update")
    public ResponseEntity<MessageResponse> AuthPasswordUpdate(@RequestBody @Valid RequestUpdatePassword requestUpdatePassword) {
        userService.updateUser(requestUpdatePassword);
        return ResponseEntity.ok(MessageResponse.of(HttpStatus.OK, "비밀번호 변경 성공", true));
    }
}
