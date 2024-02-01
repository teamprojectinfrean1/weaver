package com.task.weaver.domain.taskmember.controller;

import com.task.weaver.domain.task.dto.request.RequestCreateTask;
import com.task.weaver.domain.taskmember.dto.request.RequestCreateTaskManager;
import com.task.weaver.domain.taskmember.dto.request.RequestUpdateTaskManager;
import com.task.weaver.domain.taskmember.dto.response.ResponseTaskManager;
import com.task.weaver.domain.taskmember.service.TaskManagerService;
import com.task.weaver.domain.user.dto.reesponse.ResponseUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/taskManager")
@RequiredArgsConstructor
public class TaskManagerController {

    private TaskManagerService taskManagerService;

    @GetMapping()
    public ResponseEntity<ResponseTaskManager> getTaskManager(@RequestParam Long taskManagerId){
        ResponseTaskManager taskManager = taskManagerService.getTaskManager(taskManagerId);
        return ResponseEntity.status(HttpStatus.OK).body(taskManager);
    }

    @PostMapping()
    public ResponseEntity<ResponseTaskManager> addTaskManager(@RequestBody RequestCreateTaskManager requestCreateTaskManager){
        ResponseTaskManager responseTaskManager = taskManagerService.addTaskManager(requestCreateTaskManager);
        return ResponseEntity.status(HttpStatus.OK).body(responseTaskManager);
    }

    @PutMapping()
    public ResponseEntity<ResponseTaskManager> updateTaskManager(@RequestParam Long taskManagerId,
                                                   @RequestBody RequestUpdateTaskManager requestUpdateTaskManager){
        ResponseTaskManager responseTaskManager = taskManagerService.updateTaskManager(taskManagerId, requestUpdateTaskManager);
        return ResponseEntity.status(HttpStatus.OK).body(responseTaskManager);
    }

    @DeleteMapping()
    public ResponseEntity<String> deleteTaskManager(@RequestParam Long taskManagerId){
        taskManagerService.deleteTaskManager(taskManagerId);
        return ResponseEntity.status(HttpStatus.OK).body("taskManager deleted");
    }
}
