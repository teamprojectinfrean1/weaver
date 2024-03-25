package com.task.weaver.domain.user.controller;

import com.task.weaver.common.response.DataResponse;
import com.task.weaver.common.response.MessageResponse;
import com.task.weaver.domain.mail.annotation.CustomEmail;
import com.task.weaver.domain.user.dto.request.RequestCreateUser;
import com.task.weaver.domain.user.dto.request.RequestUpdateUser;
import com.task.weaver.domain.user.dto.response.ResponseGetUserList;
import com.task.weaver.domain.user.dto.response.ResponseUser;
import com.task.weaver.domain.user.dto.response.EmailVerificationResult;
import com.task.weaver.domain.user.service.UserService;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "User Controller", description = "사용자 관련 컨트롤러")
public class UserController {
    private final UserService userService;

    @Operation(summary = "사용자 한 명 조회", description = "사용자 한명을 조회")
    @Parameter(name = "userId", description = "사용자 id", in = ParameterIn.QUERY)
    @GetMapping()
    public ResponseEntity<ResponseUser> getUser(@RequestParam("userId") UUID userId){
        ResponseUser responseUser = userService.getUser(userId);
        return ResponseEntity.status(HttpStatus.OK).body(responseUser);
    }
//    @Operation(summary = "이메일 중복 체크", description = "이메일 중복을 체크")
//    @GetMapping("/checkMail")
//    @Parameter(name = "email", description = "이메일 입력", in = ParameterIn.QUERY)
//    public ResponseEntity<Boolean> checkMail(@RequestParam("email") String email){
//        return ResponseEntity.ok().body(userService.checkMail(email));
//    }
//    @Operation(summary = "아이디 중복 체크", description = "아이디 중복을 체크")
//    @GetMapping("/checkId")
//    @Parameter(name = "id", description = "아이디 입력", in = ParameterIn.QUERY)
//    public ResponseEntity<Boolean> checkId(@RequestParam("id") String id){
//        return ResponseEntity.ok().body(userService.checkId(id));
//    }

    @Operation(summary = "회원가입", description = "사용자가 회원가입")
    @PostMapping("/join")
    public ResponseEntity<ResponseUser> addUser(@RequestBody RequestCreateUser requestCreateUser){
        log.info("controller - join - before");
        ResponseUser responseUser = userService.addUser(requestCreateUser);
        log.info("controller - join - after");
        return ResponseEntity.status(HttpStatus.OK).body(responseUser);
    }

    @Operation(summary = "프로젝트 구성원 조회", description = "프로젝트에 소속된 인원들 조회")
    @Parameter(name = "projectId", description = "프로젝트 id", in = ParameterIn.PATH)
    @GetMapping("/{projectId}")
    public ResponseEntity<DataResponse<List<ResponseGetUserList>>> getUsersForProject(@PathVariable("projectId") UUID projectId){
        List<ResponseGetUserList> responseGetUserLists = userService.getUsers(projectId);
        return new ResponseEntity<>(DataResponse.of(HttpStatus.OK, "프로젝트 구성원 조회 성공", responseGetUserLists), HttpStatus.OK);
    }
    @Operation(summary = "사용자 정보 수정", description = "사용자의 정보를 수정")
    @Parameter(name = "userId", description = "사용자 id", in = ParameterIn.QUERY)
    @PutMapping()
    public ResponseEntity<ResponseUser> updateUser(@RequestParam("userId") UUID userId,
                                                   @RequestBody RequestUpdateUser requestUpdateUser) {
        ResponseUser responseUser = userService.updateUser(userId, requestUpdateUser);
        return ResponseEntity.status(HttpStatus.OK).body(responseUser);
    }
    @Operation(summary = "사용자 삭제", description = "사용자 정보 삭제, 사용자는 사용 불가")
    @Parameter(name = "userId", description = "사용자 id", in = ParameterIn.QUERY)
    @DeleteMapping()
    public ResponseEntity<String> deleteUser(@RequestParam("userId") UUID userId){
        userService.deleteUser(userId);
        return ResponseEntity.status(HttpStatus.OK).body("user deleted");
    }

    @PostMapping("/emails/verification-requests")
    public ResponseEntity<MessageResponse> sendMessage(@RequestParam("email") @Valid @CustomEmail String email) {
        userService.sendCodeToEmail(email);

        return new ResponseEntity<>(MessageResponse.of(HttpStatus.OK, "메시지 전송 성공"), HttpStatus.OK);
    }

    @GetMapping("/emails/verifications")
    public ResponseEntity<DataResponse<EmailVerificationResult>> verificationEmail(@RequestParam("email") @Valid @CustomEmail String email,
                                                          @RequestParam("code") String authCode) {
        EmailVerificationResult response = userService.verifiedCode(email, authCode);
        return new ResponseEntity<>(DataResponse.of(HttpStatus.OK, "이메일 검증 성공", response), HttpStatus.OK);
    }
}
