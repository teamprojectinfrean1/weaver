package com.task.weaver.domain.project.service.impl;

import static com.task.weaver.common.exception.ErrorCode.PROJECT_NOT_FOUND;
import static com.task.weaver.common.exception.ErrorCode.USER_NOT_FOUND;

import com.task.weaver.common.exception.member.UserNotFoundException;
import com.task.weaver.common.exception.project.ProjectNotFoundException;
import com.task.weaver.common.model.Permission;
import com.task.weaver.common.s3.S3Uploader;
import com.task.weaver.domain.member.entity.Member;
import com.task.weaver.domain.member.repository.MemberRepository;
import com.task.weaver.domain.project.dto.request.RequestCreateProject;
import com.task.weaver.domain.project.dto.request.RequestPageProject;
import com.task.weaver.domain.project.dto.request.RequestUpdateProject;
import com.task.weaver.domain.project.dto.response.ResponseMainAndOtherProjects;
import com.task.weaver.domain.project.dto.response.ResponseGetProject;
import com.task.weaver.domain.project.dto.response.ResponseProjectLeader;
import com.task.weaver.domain.project.dto.response.ResponseProjects;
import com.task.weaver.domain.project.dto.response.ResponsePageResult;
import com.task.weaver.domain.project.entity.Project;
import com.task.weaver.domain.project.repository.ProjectRepository;
import com.task.weaver.domain.project.service.ProjectService;
import com.task.weaver.domain.projectmember.dto.ResponseProjectMember;
import com.task.weaver.domain.projectmember.entity.ProjectMember;
import com.task.weaver.domain.projectmember.repository.ProjectMemberRepository;
import com.task.weaver.domain.task.dto.response.ResponseUpdateDetail;
import com.task.weaver.domain.userOauthMember.UserOauthMember;
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
    private static final String DIR_NAME = "images";
    private static final String PROPERTIES = "projectId";

    private final ProjectRepository projectRepository;
    private final MemberRepository memberRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final S3Uploader s3Uploader;

    @Override
    public ResponsePageResult<RequestCreateProject, Project> fetchPagedProjects(final RequestPageProject requestPageProject) {
        Pageable pageable = requestPageProject.getPageable(Sort.by(PROPERTIES).descending());
        Page<Project> result = projectRepository.findAll(pageable);
        Function<Project, RequestCreateProject> fn = (this::entityToDto);
        return new ResponsePageResult<>(result, fn);
    }

    @Override
    public List<ResponseGetProject> fetchAllProjectsForDeveloper() {
        List<Project> projects = projectRepository.findAll();
        return projects.parallelStream()
                .map(project -> new ResponseGetProject(project, null)).toList();
    }

    @Override
    public ResponseMainAndOtherProjects fetchMainAndOtherProjects(UUID memberId) {
        Member member = getMemberById(memberId);
        List<ProjectMember> projects = getProjectsByMember(member);
        Map<Boolean, List<ResponseProjects>> projectListMap = partitionProjects(member, projects);
        return getMainAndOtherProjects(projectListMap);
    }

    private ResponseMainAndOtherProjects getMainAndOtherProjects(
            final Map<Boolean, List<ResponseProjects>> projectListMap) {
        ResponseMainAndOtherProjects projects = new ResponseMainAndOtherProjects();
        projects.setMainProject(projectListMap.get(MAIN_PROJECT));
        projects.setNoMainProject(projectListMap.get(OTHER_PROJECT));
        return projects;
    }

    private Map<Boolean, List<ResponseProjects>> partitionProjects(final Member member,
                                                                   final List<ProjectMember> projectMembers) {
        return projectMembers.stream()
                .map(projectMember -> getResponseProjects(member, projectMember))
                .collect(Collectors.partitioningBy(
                        projects -> containsMemberUuid(member, projects),
                        Collectors.toList()
                ));
    }

    private boolean containsMemberUuid(final Member member, final ResponseProjects responseProjects) {
        return responseProjects.getProjectId()
                .equals(member.getMainProject().getProjectId());
    }

    private ResponseProjects getResponseProjects(final Member member,
                                                 final ProjectMember projectMember) {
        return new ResponseProjects(projectMember.getProject(),
                projectMember.getPermission(), member.getMainProject().getProjectId());
    }

    @Override
    public ResponseGetProject fetchProject(final UUID projectId) {
        Project project = getProjectById(projectId);
        UserOauthMember modifier = project.getModifier().resolveMemberByLoginType();
        Member leaders = project.getLeaders().getMember();
        return new ResponseGetProject(project, ResponseUpdateDetail.of(modifier, project.getModDate()
        ), ResponseProjectLeader.of(leaders));
    }

    @Override
    @Transactional
    public UUID addProject(final RequestCreateProject dto, MultipartFile multipartFile) throws IOException {
        Member writer = getMemberById(dto.writerUuid());
        Project project = projectRepository.save(dtoToEntity(dto));
        Set<ProjectMember> projectMembers = saveProjectMembers(project, dto.writerUuid(), dto.memberUuidList());
        editProject(dto, multipartFile, writer, project, projectMembers);
        return project.getProjectId();
    }

    private void editProject(final RequestCreateProject dto, final MultipartFile multipartFile, final Member writer,
                             final Project project, final Set<ProjectMember> projectMembers) throws IOException {
        writer.initMainProject(project);
        editProjectDetails(dto, writer, project, projectMembers);
        profileImageUpdate(multipartFile, project);
        projectRepository.save(project);
    }

    private void editProjectDetails(final RequestCreateProject dto, final Member writer, final Project savedProject,
                                    final Set<ProjectMember> projectMembers) {
        savedProject.updateTag(dto.projectTagList());
        savedProject.setProjectMemberList(projectMembers);
        savedProject.setWriter(writer);
        savedProject.setModifier(writer);
    }

    private Set<ProjectMember> saveProjectMembers(final Project project, UUID writerId, List<UUID> memberUuidList) {
        Set<ProjectMember> projectMemberList = memberRepository.findAllById(memberUuidList).stream()
                .filter(member -> !projectMemberRepository.hasMatchedProjectAndMemberId(member.getId(), project.getProjectId()))
                .peek(member -> member.initMainProject(project))
                .map(member -> ResponseProjectMember.dtoToEntity(project, member, Permission.MEMBER))
                .collect(Collectors.toSet());
        addWriterAsLeader(project, writerId, projectMemberList);
        return projectMemberList;
    }

    private void addWriterAsLeader(final Project project, final UUID writerId, final Set<ProjectMember> projectMemberList) {
        projectMemberList.add(ResponseProjectMember.dtoToEntity(project, getMemberById(writerId), Permission.LEADER));
        projectMemberRepository.saveAll(projectMemberList);
    }

    @Override
    @Transactional
    public void updateProject(UUID projectId, final RequestUpdateProject dto, final MultipartFile multipartFile)
            throws IOException {
        Project project = getProjectById(projectId);
        project.updateProject(dto, getMemberById(dto.updaterUuid()));
        project.updateTag(dto.projectTagList());
        profileImageUpdate(multipartFile, project);
        projectMemberRepository.bulkDeleteProjectMembers(project, dto.memberUuidList());
        saveProjectMembers(project, dto.updaterUuid(), dto.memberUuidList());
        projectRepository.save(project);
    }

    @Override
    @Transactional
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

    @Override
    public Project getProjectById(final UUID projectId) {
        return projectRepository.findById(projectId).orElseThrow(
                () -> new ProjectNotFoundException(PROJECT_NOT_FOUND,
                        PROJECT_NOT_FOUND.getMessage()));
    }

    private List<ProjectMember> getProjectsByMember(final Member member) {
        return projectMemberRepository.findProjectMemberByMember(member);
    }
}
