package com.task.weaver.domain.project.controller;

import com.task.weaver.common.response.DataResponse;
import com.task.weaver.common.response.MessageResponse;
import com.task.weaver.domain.project.dto.request.RequestCreateProject;
import com.task.weaver.domain.project.dto.request.RequestPageProject;
import com.task.weaver.domain.project.dto.response.ResponsePageResult;
import com.task.weaver.domain.project.entity.Project;
import com.task.weaver.domain.project.service.ProjectService;
import lombok.RequiredArgsConstructor;
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

@RequestMapping("/api/v1/project")
@RestController
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService projectService;

    @GetMapping("/{projectId}")
    public ResponseEntity<DataResponse<RequestCreateProject>> getProject(@PathVariable Long projectId) {
        RequestCreateProject project = projectService.getProject(projectId);
        return new ResponseEntity<>(DataResponse.of(HttpStatus.OK, "프로젝트 조회 성공", project), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<DataResponse<ResponsePageResult<RequestCreateProject, Project>>> getProjects(@RequestBody RequestPageProject pageProject) {
        ResponsePageResult<RequestCreateProject, Project> projects = projectService.getProjects(pageProject);
        return new ResponseEntity<>(DataResponse.of(HttpStatus.OK, "프로젝트 리스트 조회 성공", projects), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<DataResponse<Long>> addProject(@RequestBody RequestCreateProject project) {
        Long aLong = projectService.addProject(project);
        return new ResponseEntity<>(DataResponse.of(HttpStatus.CREATED, "프로젝트 추가 성공", aLong), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<MessageResponse> updateProject(@RequestBody RequestCreateProject project) {
        projectService.updateProject(project);
        return new ResponseEntity<>(MessageResponse.of(HttpStatus.OK, "프로젝트 업데이트 성공"), HttpStatus.OK);
    }

    @PutMapping("/{projectId}")
    public ResponseEntity<MessageResponse> updateProject(@PathVariable Long projectId) {
        projectService.updateProjectView(projectId);
        return new ResponseEntity<>(MessageResponse.of(HttpStatus.OK, "프로젝트 뷰 업데이트 성공"), HttpStatus.OK);
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<MessageResponse> deleteProject(@PathVariable Long projectId) {
        projectService.deleteProject(projectId);
        return new ResponseEntity<>(MessageResponse.of(HttpStatus.NO_CONTENT, "프로젝트 삭제 성공"), HttpStatus.NO_CONTENT);
    }
}
