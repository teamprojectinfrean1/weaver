package com.task.weaver.domain.task.controller;

import com.task.weaver.common.response.DataResponse;
import com.task.weaver.domain.task.dto.request.RequestCreateTaskAuthority;
import com.task.weaver.domain.task.dto.request.RequestUpdateTaskAuthority;
import com.task.weaver.domain.task.dto.response.ResponseTaskAuthority;
import com.task.weaver.domain.task.service.TaskAuthorityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/taskAuthority")
@RequiredArgsConstructor
public class TaskAuthorityController {

    private final TaskAuthorityService taskAuthorityService;

    @GetMapping()
    public ResponseEntity<ResponseTaskAuthority> getTaskAuthority(@RequestParam Long taskAuthorityId){
        ResponseTaskAuthority responseTaskAuthority = taskAuthorityService.getTaskAuthority(taskAuthorityId);
        return ResponseEntity.status(HttpStatus.OK).body(responseTaskAuthority);
    }

    @PostMapping()
    public ResponseEntity<ResponseTaskAuthority> addTaskAuthority(@RequestBody RequestCreateTaskAuthority requestCreateUser){
        ResponseTaskAuthority responseTaskAuthority = taskAuthorityService.addTaskAuthority(requestCreateUser);
        return ResponseEntity.status(HttpStatus.OK).body(responseTaskAuthority);
    }

    @PutMapping()
    public ResponseEntity<ResponseTaskAuthority> updateAuthority(@RequestParam Long taskAuthorityId,
                                                   @RequestBody RequestUpdateTaskAuthority requestUpdateUser){
        ResponseTaskAuthority responseTaskAuthority = taskAuthorityService.updateTaskAuthority(taskAuthorityId, requestUpdateUser);
        return ResponseEntity.status(HttpStatus.OK).body(responseTaskAuthority);
    }

    @DeleteMapping()
    public ResponseEntity<String> deleteTask(@RequestParam Long taskAuthorityId){
        taskAuthorityService.deleteTaskAuthority(taskAuthorityId);
        return ResponseEntity.status(HttpStatus.OK).body("taskAuthority deleted");
    }
}
