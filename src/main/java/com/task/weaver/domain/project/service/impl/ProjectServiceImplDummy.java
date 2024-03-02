package com.task.weaver.domain.project.service.impl;

import com.task.weaver.common.exception.BusinessException;
import com.task.weaver.common.exception.project.ProjectNotFoundException;
import com.task.weaver.common.exception.user.UserNotFoundException;
import com.task.weaver.domain.issue.dto.response.IssueResponse;
import com.task.weaver.domain.issue.entity.Issue;
import com.task.weaver.domain.project.dto.request.RequestCreateProject;
import com.task.weaver.domain.project.dto.request.RequestPageProject;
import com.task.weaver.domain.project.dto.response.ResponseGetProject;
import com.task.weaver.domain.project.dto.response.ResponseGetProjectList;
import com.task.weaver.domain.project.dto.response.ResponsePageResult;
import com.task.weaver.domain.project.entity.Project;
import com.task.weaver.domain.project.repository.ProjectRepository;
import com.task.weaver.domain.project.service.ProjectService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import com.task.weaver.domain.projectmember.entity.ProjectMember;
import com.task.weaver.domain.projectmember.repository.ProjectMemberRepository;
import com.task.weaver.domain.task.dto.response.ResponseGetTask;
import com.task.weaver.domain.task.dto.response.ResponseTask;
import com.task.weaver.domain.task.entity.Task;
import com.task.weaver.domain.user.entity.User;
import com.task.weaver.domain.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.swing.*;

@Service
@AllArgsConstructor
public class ProjectServiceImplDummy implements ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ProjectMemberRepository projectMemberRepository;

    @Override
    public ResponsePageResult<RequestCreateProject, Project> getProjects(final RequestPageProject requestPageProject)
            throws BusinessException {

        Pageable pageable = requestPageProject.getPageable(Sort.by("projectId").descending());
        Page<Project> result = projectRepository.findAll(pageable);
        Function<Project, RequestCreateProject> fn = (this::entityToDto);

        return new ResponsePageResult<>(result, fn);
    }

    @Override
    public List<ResponseGetProjectList> getProejctsForMain(String nickname) throws BusinessException {
        List<Project> result = projectRepository.findProjectsByNickname(nickname)
                .orElseThrow(() -> new ProjectNotFoundException(new Throwable(String.valueOf(nickname))));
        List<ResponseGetProjectList> responseGetProjectLists = new ArrayList<>();

        for (Project project : result) {
            ResponseGetProjectList responseGetProjectList = new ResponseGetProjectList(project);
            responseGetProjectLists.add(responseGetProjectList);
        }

        return responseGetProjectLists;
    }

    @Override
    public ResponseGetProject getProject(final Long projectId) throws BusinessException {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(new Throwable(String.valueOf(projectId))));

        List<ResponseGetTask> ResponseTaskList = new ArrayList<>();
        List<IssueResponse> ResponseIssueList = new ArrayList<>();

        List<Task> taskList = project.getTaskList();

        for (Task task : taskList) {
            ResponseTaskList.add(new ResponseGetTask(task));

            for (Issue issue : task.getIssueList()) {
                ResponseIssueList.add(new IssueResponse(issue));
            }
        }

        ResponseGetProject responseGetProject = new ResponseGetProject(project);
        responseGetProject.setTaskList(ResponseTaskList);
        responseGetProject.setIssueList(ResponseIssueList);

        return responseGetProject;
    }

    @Override
    public Long addProject(final RequestCreateProject dto) throws BusinessException {
        Project project = dtoToEntity(dto);
        Project savedProject = projectRepository.save(project);
        List<ProjectMember> projectMemberList = new ArrayList<>();

        for (String nickname : dto.nicknames()) {
            User user = userRepository.findByNickname(nickname)
                    .orElseThrow(() -> new UserNotFoundException(new Throwable(String.valueOf(nickname))));

            ProjectMember projectMember = ProjectMember.builder()
                    .project(savedProject)
                    .user(user)
                    .build();
            projectMemberRepository.save(projectMember);
            projectMemberList.add(projectMember);
        }

        savedProject.setProjectMemberList(projectMemberList);
        projectRepository.save(savedProject);

        return savedProject.getProjectId();
    }

    @Override
    public void updateProject(Long project_id, final RequestCreateProject dto) throws BusinessException {
        Optional<Project> result = projectRepository.findById(project_id);
        if (result.isPresent()) {
            Project entity = result.get();
            entity.changeDetail(dto.detail());
            entity.changeName(dto.name());
            projectRepository.save(entity);
            return;
        }
        throw new ProjectNotFoundException(new Throwable(String.valueOf(project_id)));
    }

    @Override
    public void deleteProject(final Long projectId) throws BusinessException {
        Optional<Project> project = projectRepository.findById(projectId);
        if (project.isPresent()){
            projectRepository.deleteById(projectId);
            return;
        }
        throw new ProjectNotFoundException(new Throwable(String.valueOf(projectId)));
    }

    @Override
    public void updateProjectView(Long projectId) {
        Optional<Project> result = projectRepository.findById(projectId);
        if (result.isPresent()) {
            Project project = result.get();
            project.changePublic();
            projectRepository.save(project);
            return;
        }
        throw new ProjectNotFoundException(new Throwable(String.valueOf(projectId)));
    }
}
