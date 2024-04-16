package com.task.weaver.domain.user.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.task.weaver.common.response.DataResponse;
import com.task.weaver.domain.project.dto.response.ResponsePageResult;
import com.task.weaver.domain.user.dto.request.RequestGetUserPage;
import com.task.weaver.domain.user.dto.request.RequestUpdateUser;
import com.task.weaver.domain.user.dto.response.ResponseGetUser;
import com.task.weaver.domain.user.dto.response.ResponseGetUserForFront;
import com.task.weaver.domain.user.dto.response.ResponseGetUserList;
import com.task.weaver.domain.user.entity.User;
import com.task.weaver.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "User Controller", description = "사용자 관련 컨트롤러")
public class UserController {

    private final UserService userService;

    @Operation(summary = "사용자 한 명 조회", description = "사용자 한명을 조회")
    @Parameter(name = "userId", description = "사용자 id", in = ParameterIn.QUERY)
    @GetMapping("/{userId}")
    public ResponseEntity<ResponseGetUser> getUser(@PathVariable("userId") UUID userId){
        ResponseGetUser responseGetUser = userService.getUser(userId);
        return ResponseEntity.status(HttpStatus.OK).body(responseGetUser);
    }

    @Operation(summary = "프로젝트 구성원 조회", description = "프로젝트에 소속된 인원들 조회")
    @Parameter(name = "projectId", description = "프로젝트 id", in = ParameterIn.PATH)
    @GetMapping("/project/user-list")
    public ResponseEntity<DataResponse<ResponsePageResult<ResponseGetUserList, User>>> getUsersFromProject(@RequestBody RequestGetUserPage requestGetUserPage){
        ResponsePageResult<ResponseGetUserList, User> responseGetUserLists = userService.getUsers(requestGetUserPage);
        return new ResponseEntity<>(DataResponse.of(HttpStatus.OK, "프로젝트 구성원 조회 성공", responseGetUserLists, true), HttpStatus.OK);
    }

    @Operation(summary = "개발자용 유저 리스트 확인 api", description = "생성된 유저 전부 조회")
    @GetMapping("/list/test")
    public ResponseEntity<DataResponse<List<ResponseGetUser>>> getUsersForTest(){
        List<ResponseGetUser> responseGetUsers = userService.getUsersForTest();
        return new ResponseEntity<>(DataResponse.of(HttpStatus.OK, "유저 리스트 전부 조회", responseGetUsers, true), HttpStatus.OK);
    }

    @Operation(summary = "토큰 기반 유저 조회", description = "로그인 직후, 토큰 기반으로 유저 정보 조회")
    @Parameter(name = "Authorization", description = "토큰", in = ParameterIn.HEADER)
    @GetMapping("/token")
    public ResponseEntity<DataResponse<ResponseGetUserForFront>> getUsersFromToken(HttpServletRequest request) {
        ResponseGetUserForFront responseGetUser = userService.getUserFromToken(request);
        return new ResponseEntity<>(DataResponse.of(HttpStatus.OK, "토큰 기반 유저 정보 반환 성공", responseGetUser, true),
                HttpStatus.OK);
    }

    @Operation(summary = "사용자 정보 수정", description = "사용자의 정보 (프로필 이미지, 닉네임, 비밀번호) 업데이트")
    @Parameter(name = "userId", description = "사용자 id", in = ParameterIn.QUERY)
    @PutMapping("/update")
    public ResponseEntity<DataResponse<ResponseGetUser>> updateUser(@RequestParam("userId") UUID userId,
                                                                    @RequestBody RequestUpdateUser requestUpdateUser)
            throws ParseException, IOException {

        ResponseGetUser responseGetUser = userService.updateUser(userId, requestUpdateUser);
        return ResponseEntity.ok(DataResponse.of(HttpStatus.OK, "유저 정보 수정 성공", responseGetUser, true));
    }

    @Operation(summary = "사용자 삭제", description = "사용자 정보 삭제, 사용자는 사용 불가")
    @Parameter(name = "userId", description = "사용자 id", in = ParameterIn.QUERY)
    @DeleteMapping()
    public ResponseEntity<String> deleteUser(@RequestParam("userId") UUID userId){
        userService.deleteUser(userId);
        return ResponseEntity.status(HttpStatus.OK).body("user deleted");
    }
}
