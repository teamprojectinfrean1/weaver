package com.task.weaver.domain.project.service.impl;

import com.task.weaver.common.exception.BusinessException;
import com.task.weaver.common.exception.project.ProjectNotFoundException;
import com.task.weaver.common.exception.member.UserNotFoundException;
import com.task.weaver.common.s3.S3Uploader;
import com.task.weaver.domain.authorization.entity.Member;
import com.task.weaver.domain.project.dto.request.RequestCreateProject;
import com.task.weaver.domain.project.dto.request.RequestPageProject;
import com.task.weaver.domain.project.dto.request.RequestUpdateProject;
import com.task.weaver.domain.project.dto.response.ResponseGetProject;
import com.task.weaver.domain.project.dto.response.ResponseGetProjectList;
import com.task.weaver.domain.project.dto.response.ResponsePageResult;
import com.task.weaver.domain.project.entity.Project;
import com.task.weaver.domain.project.repository.ProjectRepository;
import com.task.weaver.domain.project.service.ProjectService;

import com.task.weaver.domain.authorization.repository.MemberRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

import com.task.weaver.domain.projectmember.entity.ProjectMember;
import com.task.weaver.domain.projectmember.repository.ProjectMemberRepository;
import com.task.weaver.domain.task.dto.response.ResponseUpdateDetail;
import com.task.weaver.domain.member.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;


@Service
@RequiredArgsConstructor
@Slf4j
public class ProjectServiceImplDummy implements ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final MemberRepository memberRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final S3Uploader s3Uploader;
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
    public List<ResponseGetProjectList> getProejctsForMain(UUID memberId) throws BusinessException {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new UserNotFoundException(new Throwable(String.valueOf(memberId))));

        List<Project> result = projectRepository.findProjectsByMember(member)
                .orElseThrow(() -> new ProjectNotFoundException(new Throwable(String.valueOf(memberId))));

        List<ResponseGetProjectList> responseGetProjectLists = new ArrayList<>();

        for (Project project : result) {
            ResponseGetProjectList responseGetProjectList = new ResponseGetProjectList(project);

            if(project.getProjectId() == member.getMainProject().getProjectId())
                responseGetProjectList.setIsMainProject(true);

            responseGetProjectLists.add(responseGetProjectList);
        }
        return responseGetProjectLists;
    }

    @Override
    public ResponseGetProject getProject(final UUID projectId) throws BusinessException {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(new Throwable(String.valueOf(projectId))));

        Member modifier = project.getModifier();

        ResponseUpdateDetail responseUpdateDetail = ResponseUpdateDetail.builder()
                .memberUuid(modifier.getId())
                .userNickname(modifier.getUser().getNickname())
                .updatedDate(project.getModDate())
                .build();

        ResponseGetProject responseGetProject = new ResponseGetProject(project);
        responseGetProject.setLastUpdateDetail(responseUpdateDetail);

        return responseGetProject;
    }

    @Transactional
    @Override
    public UUID addProject(final RequestCreateProject dto, MultipartFile multipartFile) throws BusinessException, IOException {
        Project project = dtoToEntity(dto);
        Member writer = memberRepository.findById(dto.writerUuid())
                .orElseThrow(() -> new UserNotFoundException(new Throwable(String.valueOf(dto.writerUuid()))));

        if(writer.getMainProject() == null)   //작성자가 처음 만든 프로젝트면, 메인 프로젝트로 선정
            writer.updateMainProject(project);

        Project savedProject = projectRepository.save(project);
        List<ProjectMember> projectMemberList = new ArrayList<>();

        for (UUID memberId : dto.memberUuidList()) {
            Member member = memberRepository.findById(memberId)
                    .orElseThrow(() -> new UserNotFoundException(new Throwable(String.valueOf(memberId))));

            if(member.getMainProject() == null){
                member.updateMainProject(savedProject);
            }

            ProjectMember projectMember = ProjectMember.builder()
                    .project(savedProject)
                    .member(member)
                    .build();

            projectMemberRepository.save(projectMember);
            projectMemberList.add(projectMember);
        }

        savedProject.setProjectMemberList(projectMemberList);
        savedProject.setWriter(writer);
        savedProject.setModifier(writer);

        if (multipartFile != null) {
            String storedFileName = s3Uploader.upload(multipartFile, "images");
            URL updatedImageUrlObject = new URL(storedFileName);
            savedProject.setProjectImage(updatedImageUrlObject);
        }

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
            Member updater = memberRepository.findById(dto.updaterUuid())
                .orElseThrow(() -> new UserNotFoundException(new Throwable(String.valueOf(dto.updaterUuid()))));
            Project entity = result.get();
            entity.updateProject(dto, updater);
            return;
        }
        throw new ProjectNotFoundException(new Throwable(String.valueOf(projectId)));
    }

    @Override
    @Transactional
    public void updateMainProject(UUID projectId) throws BusinessException {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(new Throwable(String.valueOf(projectId))));

        UUID writerId = project.getWriter().getId();

        Member writer = memberRepository.findById(writerId)
                .orElseThrow(() -> new UserNotFoundException(new Throwable(String.valueOf(writerId))));

        writer.updateMainProject(project);

        memberRepository.save(writer);
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
            projectRepository.save(project);
            return;
        }
        throw new ProjectNotFoundException(new Throwable(String.valueOf(projectId)));
    }
}
