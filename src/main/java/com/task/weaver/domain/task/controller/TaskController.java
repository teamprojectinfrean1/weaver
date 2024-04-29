package com.task.weaver.domain.task.controller;

import static com.task.weaver.domain.task.dto.response.ResponseTask.SimpleResponse;

import com.task.weaver.common.response.DataResponse;
import com.task.weaver.common.response.MessageResponse;
import com.task.weaver.domain.project.dto.response.ResponsePageResult;
import com.task.weaver.domain.task.dto.request.RequestCreateTask;
import com.task.weaver.domain.task.dto.request.RequestUpdateTask;
import com.task.weaver.domain.task.dto.response.ResponseGetTask;
import com.task.weaver.domain.task.dto.response.ResponseGetTaskList;
import com.task.weaver.domain.task.entity.Task;
import com.task.weaver.domain.task.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "Task Controller", description = "태스크 관련 컨트롤러")
@RestController
@RequestMapping("/api/v1/task")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @Operation(summary = "프로젝트 태스크 상세 조회", description = "태스크 하나를 조회합니다.")
    @GetMapping("/{taskId}")
    public ResponseEntity<DataResponse<ResponseGetTask>> getTask(@PathVariable("taskId") UUID taskId){
        ResponseGetTask responseTask = taskService.getTask(taskId);
        return new ResponseEntity<>(DataResponse.of(HttpStatus.OK, "태스크 상세 조회 성공", responseTask, true), HttpStatus.OK);
    }

    @Operation(summary = "프로젝트 태스크 페이지 조회", description = "프로젝트에 생성된 태스크들을 페이지로 조회합니다.")
    @GetMapping
    public ResponseEntity<DataResponse<ResponsePageResult<ResponseGetTaskList, Task>>> getTasks(@RequestParam int page,
                                                                                                @RequestParam int size,
                                                                                                @RequestParam UUID projectId) {
        log.info("Project ID ={}", projectId);
        ResponsePageResult<ResponseGetTaskList, Task> responsePageResult = taskService.getTasks(page, size, projectId);
        return new ResponseEntity<>(DataResponse.of(HttpStatus.OK, "프로젝트에 연결된 태스크들 조회 성공", responsePageResult, true),
                HttpStatus.OK);
    }

    @Operation(summary = "프로젝트 태스크 전체 조회", description = "프로젝트에 생성된 태스크를 전체 조회합니다.")
    @GetMapping("/all")
    public ResponseEntity<DataResponse<List<ResponseGetTaskList>>> getAllTask(@RequestParam UUID projectId) {
        List<ResponseGetTaskList> tasks = taskService.getTasks(projectId);
        return ResponseEntity.ok(DataResponse.of(HttpStatus.OK, "태스크 전체 조회 성공", tasks, true));
    }

    /**TODO: 2024-04-17, 수, 17:5  -JEON
    *  TASK: 태스크 생성시 조인 3번 발생 -> 2번으로 줄여야함
    */
    @Operation(summary = "태스크 생성", description = "프로젝트에 태스크 하나를 생성합니다.")
    @PostMapping()
    public ResponseEntity<DataResponse<SimpleResponse>> addTask(@RequestBody RequestCreateTask requestCreateTask){
        UUID taskId = taskService.addTask(requestCreateTask);
        return new ResponseEntity<>(DataResponse.of(HttpStatus.OK, "태스크 생성 성공", SimpleResponse.of(taskId), true), HttpStatus.OK);
    }

    @Operation(summary = "태스크 수정", description = "태스크 하나의 정보를 수정합니다.")
    @PutMapping()
    public ResponseEntity<DataResponse<ResponseGetTask>> updateTask(@RequestParam UUID taskId,
                                                   @RequestBody RequestUpdateTask requestUpdateTask){
        ResponseGetTask responseTask = taskService.updateTask(taskId, requestUpdateTask);
        return new ResponseEntity<>(DataResponse.of(HttpStatus.OK, "태스크 수정 성공", responseTask, true), HttpStatus.OK);
    }

    @PutMapping("/status")
    @Operation(summary = "태크스 상태 변경", description = "태스크 하나의 상태를 변경합니다. 진행중, 완료")
    @Parameter(name = "taskId", description = "태크스 id값", in = ParameterIn.QUERY)
    @Parameter(name = "status", description = "상태 이름 값", in = ParameterIn.QUERY)
    public ResponseEntity<DataResponse<ResponseGetTask>> updateTaskStatus(@RequestParam("taskId") UUID taskId, @RequestParam("status") String status) {
        ResponseGetTask responseGetTask = taskService.updateTaskStatus(taskId, status);
        return new ResponseEntity<>(DataResponse.of(HttpStatus.OK, "태크스 상태 수정 성공", responseGetTask, true), HttpStatus.OK);
    }

    @Operation(summary = "태스크 삭제", description = "태스크 하나를 삭제합니다.")
    @DeleteMapping()
    public ResponseEntity<MessageResponse> deleteTask(@RequestParam UUID taskId){
        taskService.deleteTask(taskId);
        return new ResponseEntity<>(MessageResponse.of(HttpStatus.OK, "태스크 삭제 성공", true), HttpStatus.OK);
    }
}