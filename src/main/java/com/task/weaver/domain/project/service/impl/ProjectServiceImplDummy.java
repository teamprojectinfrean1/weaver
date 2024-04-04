package com.task.weaver.domain.project.service.impl;

import com.task.weaver.common.exception.BusinessException;
import com.task.weaver.common.exception.project.ProjectNotFoundException;
import com.task.weaver.common.exception.user.UserNotFoundException;
import com.task.weaver.domain.project.dto.request.RequestCreateProject;
import com.task.weaver.domain.project.dto.request.RequestPageProject;
import com.task.weaver.domain.project.dto.request.RequestUpdateProject;
import com.task.weaver.domain.project.dto.response.ResponseGetProject;
import com.task.weaver.domain.project.dto.response.ResponseGetProjectList;
import com.task.weaver.domain.project.dto.response.ResponsePageResult;
import com.task.weaver.domain.project.entity.Project;
import com.task.weaver.domain.project.repository.ProjectRepository;
import com.task.weaver.domain.project.service.ProjectService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

import com.task.weaver.domain.projectmember.entity.ProjectMember;
import com.task.weaver.domain.projectmember.repository.ProjectMemberRepository;
import com.task.weaver.domain.user.entity.User;
import com.task.weaver.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
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
    public List<ResponseGetProject> getProjectsForTest() {
        List<Project> projects = projectRepository.findAll();
        List<ResponseGetProject> projectList = new ArrayList<>();

        for (Project project : projects) {
            ResponseGetProject responseGetProject = new ResponseGetProject(project);
            projectList.add(responseGetProject);
        }
        return projectList;
    }

    @Override
    public List<ResponseGetProjectList> getProejctsForMain(UUID userId) throws BusinessException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(new Throwable(String.valueOf(userId))));

        List<Project> result = projectRepository.findProjectsByUser(user)
                .orElseThrow(() -> new ProjectNotFoundException(new Throwable(String.valueOf(userId))));

        List<ResponseGetProjectList> responseGetProjectLists = new ArrayList<>();

        for (Project project : result) {
            ResponseGetProjectList responseGetProjectList = new ResponseGetProjectList(project);

            if(project.getProjectId() == user.getMainProject().getProjectId())
                responseGetProjectList.setIsMainProject(true);

            responseGetProjectLists.add(responseGetProjectList);
        }

        return responseGetProjectLists;
    }

    @Override
    public ResponseGetProject getProject(final UUID projectId) throws BusinessException {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(new Throwable(String.valueOf(projectId))));

        User modifier = project.getModifier();

        ResponseUpdateDetail responseUpdateDetail = ResponseUpdateDetail.builder()
                .userUuid(modifier.getUserId())
                .userNickname(modifier.getNickname())
                .updatedDate(project.getModDate())
                .build();

        ResponseGetProject responseGetProject = new ResponseGetProject(project);
        responseGetProject.setLastUpdateDetail(responseUpdateDetail);

        return responseGetProject;
    }

    @Override
    public UUID addProject(final RequestCreateProject dto) throws BusinessException {
        Project project = dtoToEntity(dto);
        User writer = userRepository.findById(dto.writerUuid())
                .orElseThrow(() -> new UserNotFoundException(new Throwable(String.valueOf(dto.writerUuid()))));

        if(writer.getMainProject() == null)   //작성자가 처음 만든 프로젝트면, 메인 프로젝트로 선정
            writer.setMainProject(project);

        Project savedProject = projectRepository.save(project);
        List<ProjectMember> projectMemberList = new ArrayList<>();

        for (UUID userId : dto.memberUuidList()) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new UserNotFoundException(new Throwable(String.valueOf(userId))));

            if(user.getMainProject() == null){
                user.setMainProject(savedProject);
            }

            ProjectMember projectMember = ProjectMember.builder()
                    .project(savedProject)
                    .user(user)
                    .build();

            projectMemberRepository.save(projectMember);
            projectMemberList.add(projectMember);
        }

        savedProject.setProjectMemberList(projectMemberList);
        savedProject.setWriter(writer);
        savedProject.setModifier(writer);

        log.info("project uuid : " + savedProject.getProjectId());
        projectRepository.save(savedProject);

        return savedProject.getProjectId();
    }

    @Override
    public void updateProjectView(final Long projectId) throws BusinessException {

    }

    @Override
    public void updateProject(UUID projectId, final RequestUpdateProject dto) throws BusinessException {
        Optional<Project> result = projectRepository.findById(projectId);

        if (result.isPresent()) {
            User updater = userRepository.findById(dto.updaterUuid())
                .orElseThrow(() -> new UserNotFoundException(new Throwable(String.valueOf(dto.updaterUuid()))));
            Project entity = result.get();
//            entity.changeDetail(dto.projectContent());
//            entity.changeName(dto.projectName());
            entity.updateProject(dto, updater);
//            projectRepository.save(entity);
            return;
        }
        throw new ProjectNotFoundException(new Throwable(String.valueOf(projectId)));
    }

    @Override
    @Transactional
    public void updateMainProject(UUID projectId) throws BusinessException {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(new Throwable(String.valueOf(projectId))));

        UUID writerId = project.getWriter().getUserId();

        User writer = userRepository.findById(writerId)
                .orElseThrow(() -> new UserNotFoundException(new Throwable(String.valueOf(writerId))));

        writer.setMainProject(project);

        userRepository.save(writer);
        return ;
    }

    @Override
    @Transactional
    public void deleteProject(final UUID projectId) throws BusinessException {
        Optional<Project> project = projectRepository.findById(projectId);
        if (project.isPresent()){
            projectRepository.deleteById(projectId);
            return;
        }
        throw new ProjectNotFoundException(new Throwable(String.valueOf(projectId)));
    }

    @Override
    public void updateProjectView(UUID projectId) {
        Optional<Project> result = projectRepository.findById(projectId);
        if (result.isPresent()) {
            Project project = result.get();
//            project.changePublic();
            projectRepository.save(project);
            return;
        }
        throw new ProjectNotFoundException(new Throwable(String.valueOf(projectId)));
    }
}
