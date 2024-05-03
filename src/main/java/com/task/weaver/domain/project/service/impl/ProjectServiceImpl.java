package com.task.weaver.domain.project.service.impl;

import static com.task.weaver.common.exception.ErrorCode.PROJECT_NOT_FOUND;
import static com.task.weaver.common.exception.ErrorCode.USER_NOT_FOUND;

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
import java.util.List;
import java.util.Map;
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
public class ProjectServiceImpl implements ProjectService {
    private static final boolean MAIN_PROJECT = true;
    private static final boolean OTHER_PROJECT = false;
    private static final int FIND = 0;
    private static final String DIR_NAME = "images";
    private static final String PROPERTIES = "projectId";

    private final ProjectRepository projectRepository;
    private final MemberRepository memberRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final S3Uploader s3Uploader;

    @Override
    public ResponsePageResult<RequestCreateProject, Project> getProjects(final RequestPageProject requestPageProject) {
        Pageable pageable = requestPageProject.getPageable(Sort.by(PROPERTIES).descending());
        Page<Project> result = projectRepository.findAll(pageable);
        Function<Project, RequestCreateProject> fn = (this::entityToDto);
        return new ResponsePageResult<>(result, fn);
    }

    @Override
    public List<ResponseGetProject> getProjectsForTest() {
        List<Project> projects = projectRepository.findAll();
        return projects.parallelStream()
                .map(project -> new ResponseGetProject(project, null)).toList();
    }

    @Override
    public ResponseGetMainProjectList getProejctsForMain(UUID memberId) {

        Member member = getMemberById(memberId);
        List<ProjectMember> projects = getProjectsByMember(member);

        Map<Boolean, List<ResponseGetProjectList>> projectListMap = getMainAndOtherProjectLists(member, projects);

        ResponseGetProjectList mainProject = projectListMap.get(MAIN_PROJECT).get(FIND);
        List<ResponseGetProjectList> noMainProjects = projectListMap.get(OTHER_PROJECT);

        ResponseGetMainProjectList mainProjectList = new ResponseGetMainProjectList();
        mainProjectList.setMainProject(mainProject);
        mainProjectList.setNoMainProject(noMainProjects);
        return mainProjectList;
    }

    private Map<Boolean, List<ResponseGetProjectList>> getMainAndOtherProjectLists(final Member member,
                                                                                   final List<ProjectMember> projects) {
        return projects.stream()
                .map(projectMember -> new ResponseGetProjectList(projectMember.getProject(),
                        projectMember.getPermission(), member.getMainProject().getProjectId()))
                .collect(Collectors.partitioningBy(
                        responseGetProjectList -> responseGetProjectList.getProjectId()
                                .equals(member.getMainProject().getProjectId()),
                        Collectors.toList()
                ));
    }

    @Override
    public ResponseGetProject getProject(final UUID projectId) {
        Project project = getProjectById(projectId);
        Member modifier = project.getModifier();
        ResponseUpdateDetail responseUpdateDetail = ResponseUpdateDetail.of(modifier.resolveMemberByLoginType(),
                project.getModDate());
        return new ResponseGetProject(project, responseUpdateDetail);
    }

    @Transactional
    @Override
    public UUID addProject(final RequestCreateProject dto, MultipartFile multipartFile) throws IOException {
        Member writer = getMemberById(dto.writerUuid());
        Project project = projectRepository.save(dtoToEntity(dto));
        Set<ProjectMember> projectMembers = saveProjectMembers(project, dto);

        isFirstProject(project, writer);
        updateProjectDetails(dto, writer, project, projectMembers);
        profileImageUpdate(multipartFile, project);
        projectRepository.save(project);
        return project.getProjectId();
    }

    private void updateProjectDetails(final RequestCreateProject dto, final Member writer, final Project savedProject,
                                      final Set<ProjectMember> projectMembers) {
        savedProject.updateTag(dto.projectTagList());
        savedProject.setProjectMemberList(projectMembers);
        savedProject.setWriter(writer);
        savedProject.setModifier(writer);
    }

    private Set<ProjectMember> saveProjectMembers(final Project project, RequestCreateProject dto) {
        UUID writerId = dto.writerUuid();
        Set<ProjectMember> projectMemberList = dto.memberUuidList().parallelStream()
                .map(memberId -> {
                    Member member = getMemberById(memberId);
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
        Project project = getProjectById(projectId);
        Member updater = getMemberById(dto.updaterUuid());
        project.updateProject(dto, updater);
        project.updateTag(dto.projectTagList());
        profileImageUpdate(multipartFile, project);
        projectRepository.save(project);
    }

    @Transactional
    @Override
    public void updateMainProject(UUID projectId) {
        Project project = getProjectById(projectId);
        UUID writerId = project.getWriter().getId();
        Member writer = getMemberById(writerId);
        writer.updateMainProject(project);
        memberRepository.save(writer);
    }

    @Override
    @Transactional
    public void deleteProject(final UUID projectId) {
        projectRepository.deleteById(projectId);
    }

    @Override
    public void updateProjectView(UUID projectId) {
        Project result = getProjectById(projectId);
        projectRepository.save(result);
    }

    private void profileImageUpdate(final MultipartFile multipartFile, final Project project) throws IOException {
        if (multipartFile != null) {
            String storedFileName = s3Uploader.upload(multipartFile, DIR_NAME);
            URL updatedImageUrlObject = new URL(storedFileName);
            project.setProjectImage(updatedImageUrlObject);
        }
    }

    private Member getMemberById(final UUID memberUuid) {
        return memberRepository.findById(memberUuid)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND,
                        USER_NOT_FOUND.getMessage()));
    }

    private Project getProjectById(final UUID projectId) {
        return projectRepository.findById(projectId).orElseThrow(
                () -> new ProjectNotFoundException(PROJECT_NOT_FOUND,
                        PROJECT_NOT_FOUND.getMessage()));
    }

    private List<ProjectMember> getProjectsByMember(final Member member) {
        return projectMemberRepository.findProjectsByMember(member)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND, USER_NOT_FOUND.getMessage()));
    }
}
