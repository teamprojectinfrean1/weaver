package com.task.weaver.domain.user.controller;

import com.task.weaver.domain.user.dto.request.RequestCreateUser;
import com.task.weaver.domain.user.dto.request.RequestUpdateUser;
import com.task.weaver.domain.user.dto.response.ResponseUser;
import com.task.weaver.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @GetMapping()
    public ResponseEntity<ResponseUser> getUser(@RequestParam Long user_id){
        ResponseUser responseUser = userService.getUser(user_id);
        return ResponseEntity.status(HttpStatus.OK).body(responseUser);
    }

    @PostMapping()
    public ResponseEntity<ResponseUser> addUser(@RequestBody RequestCreateUser requestCreateUser){
        ResponseUser responseUser = userService.addUser(requestCreateUser);
        return ResponseEntity.status(HttpStatus.OK).body(responseUser);
    }

    @PutMapping()
    public ResponseEntity<ResponseUser> updateUser(@RequestParam Long user_id,
                                                   @RequestBody RequestUpdateUser requestUpdateUser){
        ResponseUser responseUser = userService.updateUser(user_id, requestUpdateUser);
        return ResponseEntity.status(HttpStatus.OK).body(responseUser);
    }
    @DeleteMapping()
    public ResponseEntity<String> deleteUser(@RequestParam Long user_id){
        userService.deleteUser(user_id);
        return ResponseEntity.status(HttpStatus.OK).body("user deleted");
    }
}
