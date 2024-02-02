package com.task.weaver.domain.task.controller;

import com.task.weaver.domain.task.dto.request.RequestCreateTask;
import com.task.weaver.domain.task.dto.request.RequestUpdateTask;
import com.task.weaver.domain.task.dto.response.ResponseTask;
import com.task.weaver.domain.task.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/task")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;
    @GetMapping()
    public ResponseEntity<ResponseTask> getTask(@RequestParam Long taskId){
        ResponseTask responseTask = taskService.getTask(taskId);
        return ResponseEntity.status(HttpStatus.OK).body(responseTask);
    }

    @PostMapping()
    public ResponseEntity<ResponseTask> addTask(@RequestBody RequestCreateTask requestCreateTask){
        ResponseTask responseTask = taskService.addTask(requestCreateTask);
        return ResponseEntity.status(HttpStatus.OK).body(responseTask);
    }

    @PutMapping()
    public ResponseEntity<ResponseTask> updateTask(@RequestParam Long taskId,
                                                   @RequestBody RequestUpdateTask requestUpdateTask){
        ResponseTask responseTask = taskService.updateTask(taskId, requestUpdateTask);
        return ResponseEntity.status(HttpStatus.OK).body(responseTask);
    }

    @DeleteMapping()
    public ResponseEntity<String> deleteTask(@RequestParam Long taskId){
        taskService.deleteTask(taskId);
        return ResponseEntity.status(HttpStatus.OK).body("task deleted");
    }
}