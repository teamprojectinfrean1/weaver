package com.task.weaver.domain.project.controller;

import com.task.weaver.common.response.DataResponse;
import com.task.weaver.common.response.MessageResponse;
import com.task.weaver.domain.project.dto.request.RequestCreateProject;
import com.task.weaver.domain.project.dto.request.RequestPageProject;
import com.task.weaver.domain.project.dto.response.ResponseGetProject;
import com.task.weaver.domain.project.dto.response.ResponseGetProjectList;
import com.task.weaver.domain.project.dto.response.ResponsePageResult;
import com.task.weaver.domain.project.entity.Project;
import com.task.weaver.domain.project.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.datetime.DateFormatterRegistrar;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;

@Tag(name = "Project Controller", description = "프로젝트 관련 컨트롤러")
@RequestMapping("/api/v1/project")
@RestController
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService projectService;

    @Operation(summary = "프로젝트 단건 조회", description = "프로젝트 단건 조회")
    @GetMapping("/{projectId}")
    public ResponseEntity<DataResponse<ResponseGetProject>> getProject(@PathVariable Long projectId) {
        ResponseGetProject project = projectService.getProject(projectId);
        return new ResponseEntity<>(DataResponse.of(HttpStatus.OK, "프로젝트 조회 성공", project), HttpStatus.OK);
    }
    @Operation(summary = "프로젝트 다수 조회", description = "프로젝트 페이지로 조회")
    @GetMapping
    public ResponseEntity<DataResponse<ResponsePageResult<RequestCreateProject, Project>>> getProjects(@RequestBody RequestPageProject pageProject) {
        ResponsePageResult<RequestCreateProject, Project> projects = projectService.getProjects(pageProject);
        return new ResponseEntity<>(DataResponse.of(HttpStatus.OK, "프로젝트 리스트 조회 성공", projects), HttpStatus.OK);
    }
    @Operation(summary = "닉네임 기반으로 프로젝트 리스트 조회", description = "로그인한 사용자가 참여한 프로젝트들을 조회")
    @GetMapping("/{nickname}")
    public ResponseEntity<DataResponse<List<ResponseGetProjectList>>> getProjects(@PathVariable String nickname){
        List<ResponseGetProjectList> projects = projectService.getProejctsForMain(nickname);
        return new ResponseEntity<>(DataResponse.of(HttpStatus.OK, "유저의 프로젝트 리스트 조회 성공", projects), HttpStatus.OK);
    }
    @Operation(summary = "프로젝트 생성", description = "프로젝트 생성")
    @PostMapping
    public ResponseEntity<DataResponse<Long>> addProject(@RequestBody RequestCreateProject project) {
        Long aLong = projectService.addProject(project);
        return new ResponseEntity<>(DataResponse.of(HttpStatus.CREATED, "프로젝트 추가 성공", aLong), HttpStatus.CREATED);
    }
    @Operation(summary = "프로젝트 업데이트", description = "프로젝트 정보 수정")
    @PutMapping("/{project_id}")
    public ResponseEntity<MessageResponse> updateProject(@PathVariable Long project_id, @RequestBody RequestCreateProject project) {
        projectService.updateProject(project_id, project);
        return new ResponseEntity<>(MessageResponse.of(HttpStatus.OK, "프로젝트 업데이트 성공"), HttpStatus.OK);
    }
    @Operation(summary = "프로젝트 뷰 업데이트", description = "프로젝트 뷰 업데이트")
    @PutMapping("/{projectId}")
    public ResponseEntity<MessageResponse> updateProject(@PathVariable Long projectId) {
        projectService.updateProjectView(projectId);
        return new ResponseEntity<>(MessageResponse.of(HttpStatus.OK, "프로젝트 뷰 업데이트 성공"), HttpStatus.OK);
    }
    @Operation(summary = "프로젝트 삭제", description = "프로젝트 삭제")
    @DeleteMapping("/{projectId}")
    public ResponseEntity<MessageResponse> deleteProject(@PathVariable Long projectId) {
        projectService.deleteProject(projectId);
        return new ResponseEntity<>(MessageResponse.of(HttpStatus.NO_CONTENT, "프로젝트 삭제 성공"), HttpStatus.NO_CONTENT);
    }
}
