package com.task.weaver.domain.project.service;

import com.task.weaver.domain.project.dto.request.RequestCreateProject;
import com.task.weaver.domain.project.dto.request.RequestPageProject;
import com.task.weaver.domain.project.dto.request.RequestUpdateProject;
import com.task.weaver.domain.project.dto.response.ResponseGetProject;
import com.task.weaver.domain.project.dto.response.ResponseMainAndOtherProjects;
import com.task.weaver.domain.project.dto.response.ResponsePageResult;
import com.task.weaver.domain.project.entity.Project;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

public interface ProjectService {

    default Project dtoToEntity(RequestCreateProject dto) {
        return Project.builder()
                .name(dto.projectName())
                .detail(dto.projectContent())
                .startDate(dto.startDate().orElse(null))
                .endDate(dto.endDate().orElse(null))
                .build();
    }

    default RequestCreateProject entityToDto(Project entity) {
        return RequestCreateProject.builder()
                .projectName(entity.getName())
                .projectContent(entity.getDetail())
                .startDate(Optional.ofNullable(entity.getStartDate()))
                .endDate(Optional.ofNullable(entity.getEndDate()))
                .build();
    }

    ResponsePageResult<RequestCreateProject, Project> fetchPagedProjects(RequestPageProject requestPageProject);

    List<ResponseGetProject> fetchAllProjectsForDeveloper();

    ResponseMainAndOtherProjects fetchMainAndOtherProjects(UUID userId);

    ResponseGetProject fetchProject(UUID projectId);

    UUID addProject(RequestCreateProject dto, MultipartFile multipartFile) throws IOException;

    void updateProject(UUID projectId, RequestUpdateProject dto, MultipartFile multipartFile)
            throws IOException;

    void updateMainProject(UUID projectId);

    void deleteProject(UUID projectId);

    Project getProjectById(final UUID projectId);
}
