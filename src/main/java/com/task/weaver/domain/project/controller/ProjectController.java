package com.task.weaver.domain.project.controller;

import com.task.weaver.common.response.DataResponse;
import com.task.weaver.common.response.MessageResponse;
import com.task.weaver.domain.project.dto.request.RequestCreateProject;
import com.task.weaver.domain.project.dto.request.RequestUpdateProject;
import com.task.weaver.domain.project.dto.response.ResponseGetProject;
import com.task.weaver.domain.project.dto.response.ResponseGetProjectList;
import com.task.weaver.domain.project.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Tag(name = "Project Controller", description = "프로젝트 관련 컨트롤러")
@RequestMapping("/api/v1/project")
@RestController
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService projectService;

    @Operation(summary = "프로젝트 단건 조회", description = "프로젝트 단건 조회")
    @GetMapping("/{projectId}")
    @Parameter(name = "projectId", description = "프로젝트 id", in = ParameterIn.PATH)
    public ResponseEntity<DataResponse<ResponseGetProject>> getProject(@PathVariable("projectId") UUID projectId) {
        ResponseGetProject project = projectService.getProject(projectId);
        return new ResponseEntity<>(DataResponse.of(HttpStatus.OK, "프로젝트 조회 성공", project, true), HttpStatus.OK);
    }
//    @Operation(summary = "프로젝트 다수 조회", description = "프로젝트 페이지로 조회")
//    @GetMapping("/{userId}")
//    public ResponseEntity<DataResponse<List<ResponseGetProjectList> getProjects(@PathVariable("userId") String userId) {
//        ResponsePageResult<RequestCreateProject, Project> projects = projectService.getProjects(userId);
//        return new ResponseEntity<>(DataResponse.of(HttpStatus.OK, "프로젝트 리스트 조회 성공", projects), HttpStatus.OK);
//    }

    @Operation(summary = "유저 id 기반으로 프로젝트 리스트 조회", description = "로그인한 사용자가 참여한 프로젝트들을 조회")
    @GetMapping("/list/{memberId}")
    @Parameter(name = "memberId", description = "멤버 id", in = ParameterIn.PATH)
    public ResponseEntity<DataResponse<List<ResponseGetProjectList>>> getProjects(@PathVariable("memberId") UUID memberId){
        List<ResponseGetProjectList> projects = projectService.getProejctsForMain(memberId);
        return new ResponseEntity<>(DataResponse.of(HttpStatus.OK, "유저의 프로젝트 리스트 조회 성공", projects, true), HttpStatus.OK);
    }

    @Operation(summary = "개발자용 프로젝트 리스트 확인 api", description = "사용되지 않는 api로, 모든 프로젝트를 가져옵니다.")
    @GetMapping("/list/test")
    public ResponseEntity<DataResponse<List<ResponseGetProject>>> getProjectsForTest(){
        List<ResponseGetProject> projects = projectService.getProjectsForTest();

        return new ResponseEntity<>(DataResponse.of(HttpStatus.OK, "모든 프로젝트 조회", projects, true), HttpStatus.OK);
    }
    @Operation(summary = "프로젝트 생성", description = "프로젝트 생성")
    @PostMapping()
    public ResponseEntity<DataResponse<UUID>> addProject(@RequestBody RequestCreateProject project, MultipartHttpServletRequest multipartHttpServletRequest) throws IOException {
        MultipartFile multipartFile = multipartHttpServletRequest.getFile("projectImage");
        UUID aLong = projectService.addProject(project, multipartFile);
        return new ResponseEntity<>(DataResponse.of(HttpStatus.CREATED, "프로젝트 추가 성공", aLong, true), HttpStatus.CREATED);
    }

    @Operation(summary = "메인 프로젝트 변경", description = "메인 프로젝트를 변경합니다.")
    @Parameter(name = "projectId" , description = "프로젝트 uuid", in = ParameterIn.PATH)
    @PutMapping("main-project/{projectId}")
    public ResponseEntity<DataResponse<UUID>> updateMainProject(@PathVariable("projectId") UUID projectId){
        projectService.updateMainProject(projectId);
        return new ResponseEntity<>(DataResponse.of(HttpStatus.OK, "메인 프로젝트 변경 완료", projectId, true), HttpStatus.OK);
    }

    @Operation(summary = "프로젝트 업데이트", description = "프로젝트 정보 수정")
    @PutMapping("/{projectId}")
    @Parameter(name = "projectId", description = "프로젝트 uuid", in = ParameterIn.PATH)
    public ResponseEntity<MessageResponse> updateProject(@PathVariable("projectId") UUID projectId, @RequestBody RequestUpdateProject project) {
        projectService.updateProject(projectId, project);
        return new ResponseEntity<>(MessageResponse.of(HttpStatus.OK, "프로젝트 업데이트 성공", true), HttpStatus.OK);
    }
//    @Operation(summary = "프로젝트 뷰 업데이트", description = "프로젝트 뷰 업데이트")
//    @PutMapping("/{projectId}")
//    public ResponseEntity<MessageResponse> updateProject(@PathVariable Long projectId) {
//        projectService.updateProjectView(projectId);
//        return new ResponseEntity<>(MessageResponse.of(HttpStatus.OK, "프로젝트 뷰 업데이트 성공"), HttpStatus.OK);
//    }
    @Operation(summary = "프로젝트 삭제", description = "프로젝트 삭제")
    @DeleteMapping("/{projectId}")
    @Parameter(name = "projectId", description = "프로젝트 id", in = ParameterIn.PATH)
    public ResponseEntity<MessageResponse> deleteProject(@PathVariable("projectId") UUID projectId) {
        projectService.deleteProject(projectId);
        return new ResponseEntity<>(MessageResponse.of(HttpStatus.NO_CONTENT, "프로젝트 삭제 성공", true), HttpStatus.NO_CONTENT);
    }
}
