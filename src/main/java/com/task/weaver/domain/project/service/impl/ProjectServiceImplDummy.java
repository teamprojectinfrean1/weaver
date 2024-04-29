package com.task.weaver.domain.project.service.impl;

import com.task.weaver.common.exception.BusinessException;
import com.task.weaver.common.exception.ErrorCode;
import com.task.weaver.common.exception.member.UserNotFoundException;
import com.task.weaver.common.exception.project.ProjectNotFoundException;
import com.task.weaver.common.s3.S3Uploader;
import com.task.weaver.domain.member.entity.Member;
import com.task.weaver.domain.member.repository.MemberRepository;
import com.task.weaver.domain.project.dto.request.RequestCreateProject;
import com.task.weaver.domain.project.dto.request.RequestPageProject;
import com.task.weaver.domain.project.dto.request.RequestUpdateProject;
import com.task.weaver.domain.project.dto.response.ResponseGetMainProjectList;
import com.task.weaver.domain.project.dto.response.ResponseGetProject;
import com.task.weaver.domain.project.dto.response.ResponseGetProjectList;
import com.task.weaver.domain.project.dto.response.ResponsePageResult;
import com.task.weaver.domain.project.entity.Project;
import com.task.weaver.domain.project.repository.ProjectRepository;
import com.task.weaver.domain.project.service.ProjectService;
import com.task.weaver.domain.projectmember.dto.ResponseProjectMember;
import com.task.weaver.domain.projectmember.entity.ProjectMember;
import com.task.weaver.domain.projectmember.repository.ProjectMemberRepository;
import com.task.weaver.domain.task.dto.response.ResponseUpdateDetail;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


@Service
@RequiredArgsConstructor
@Slf4j
public class ProjectServiceImplDummy implements ProjectService {

    private final ProjectRepository projectRepository;
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
    public ResponseGetMainProjectList getProejctsForMain(UUID memberId) throws BusinessException {

        Member member = findMember(memberId);
        List<ProjectMember> projects = projectMemberRepository.findProjectsByMember(member)
                .orElseThrow(() -> new RuntimeException(""));
        List<ResponseGetProjectList> noMainProjects = new ArrayList<>();
        ResponseGetMainProjectList responseGetMainProjectList = new ResponseGetMainProjectList();

        for (ProjectMember projectMember : projects) {
            ResponseGetProjectList responseGetProjectList = new ResponseGetProjectList(projectMember.getProject(),
                    projectMember.getPermission());

            if (projectMember.getProject().getProjectId() == member.getMainProject().getProjectId()) {
                responseGetProjectList.setIsMainProject(true);
                responseGetMainProjectList.setMainProject(responseGetProjectList);
            } else {
                noMainProjects.add(responseGetProjectList);
            }
        }
        responseGetMainProjectList.setNoMainProject(noMainProjects);
        return responseGetMainProjectList;
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
        Member writer = findMember(dto.writerUuid());
        isFirstProject(project, writer);

        Project savedProject = projectRepository.save(project);
        Set<ProjectMember> projectMembers = saveProjectMembers(savedProject, dto);

        savedProject.updateTag(dto.projectTagList());
        savedProject.setProjectMemberList(projectMembers);
        savedProject.setWriter(writer);
        savedProject.setModifier(writer);
        profileImageUpdate(multipartFile, savedProject);
        log.info("project uuid : " + savedProject.getProjectId());
        projectRepository.save(savedProject);
        return savedProject.getProjectId();
    }

    private Set<ProjectMember> saveProjectMembers(final Project project, RequestCreateProject dto) {
        UUID writerId = dto.writerUuid();
        Set<ProjectMember> projectMemberList = dto.memberUuidList().parallelStream()
                .map(memberId -> {
                    Member member = findMember(memberId);
                    isFirstProject(project, member);
                    return ResponseProjectMember.dtoToEntity(project, member, writerId, memberId);
                })
                .collect(Collectors.toSet());
        projectMemberRepository.saveAll(projectMemberList);
        return projectMemberList;
    }

    private void isFirstProject(final Project project, final Member member) {
        if (member.getMainProject() == null) {
            member.updateMainProject(project);
        }
    }

    @Override
    public void updateProject(UUID projectId, final RequestUpdateProject dto, final MultipartFile multipartFile)
            throws IOException {
        Project project = findProject(projectId);
        Member updater = findMember(dto.updaterUuid());
        project.updateProject(dto, updater);
        project.updateTag(dto.projectTagList());
        profileImageUpdate(multipartFile, project);
        projectRepository.save(project);
    }

    @Override
    @Transactional
    public void updateMainProject(UUID projectId) throws BusinessException {
        Project project = findProject(projectId);
        UUID writerId = project.getWriter().getId();
        Member writer = findMember(writerId);
        writer.updateMainProject(project);
        memberRepository.save(writer);
    }

    @Override
    @Transactional
    public void deleteProject(final UUID projectId) throws BusinessException {
        Optional<Project> project = projectRepository.findById(projectId);
        if (project.isPresent()) {
            projectRepository.deleteById(projectId);
            return;
        }
        throw new ProjectNotFoundException(new Throwable(String.valueOf(projectId)));
    }

    @Override
    public void updateProjectView(UUID projectId) {
        Project result = findProject(projectId);
        projectRepository.save(result);
    }

    private void profileImageUpdate(final MultipartFile multipartFile, final Project project) throws IOException {
        if (multipartFile != null) {
            String storedFileName = s3Uploader.upload(multipartFile, "images");
            URL updatedImageUrlObject = new URL(storedFileName);
            project.setProjectImage(updatedImageUrlObject);
        }
    }

    private Member findMember(final UUID memberUuid) {
        return memberRepository.findById(memberUuid)
                .orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND,
                        ErrorCode.USER_NOT_FOUND.getMessage()));
    }

    private Project findProject(final UUID projectId) {
        return projectRepository.findById(projectId).orElseThrow(
                () -> new ProjectNotFoundException(ErrorCode.PROJECT_NOT_FOUND,
                        ErrorCode.PROJECT_NOT_FOUND.getMessage()));
    }
}
