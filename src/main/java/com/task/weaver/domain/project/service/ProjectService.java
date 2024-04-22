package com.task.weaver.domain.project.service;

import com.task.weaver.common.exception.BusinessException;
import com.task.weaver.domain.project.dto.request.RequestCreateProject;
import com.task.weaver.domain.project.dto.request.RequestPageProject;
import com.task.weaver.domain.project.dto.request.RequestUpdateProject;
import com.task.weaver.domain.project.dto.response.ResponseGetProject;
import com.task.weaver.domain.project.dto.response.ResponseGetProjectList;
import com.task.weaver.domain.project.dto.response.ResponsePageResult;
import com.task.weaver.domain.project.entity.Project;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface ProjectService {

    default Project dtoToEntity(RequestCreateProject dto) {
        return Project.builder()
                .name(dto.projectName())
                .detail(dto.projectContent())
                .startDate(dto.startDate())
                .endDate(dto.endDate())
                .build();
    }

    default RequestCreateProject entityToDto(Project entity) {
        return RequestCreateProject.builder()
                .projectName(entity.getName())
                .projectContent(entity.getDetail())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .build();
    }

    ResponsePageResult<RequestCreateProject, Project> getProjects(RequestPageProject requestPageProject)
            throws BusinessException;

    List<ResponseGetProject> getProjectsForTest();

    List<ResponseGetProjectList> getProejctsForMain(UUID userId) throws BusinessException;

    ResponseGetProject getProject(UUID projectId) throws BusinessException;

    UUID addProject(RequestCreateProject dto, MultipartFile multipartFile) throws BusinessException, IOException;

    void updateProjectView(Long projectId) throws BusinessException;

    void updateProject(UUID projectId, RequestUpdateProject dto) throws BusinessException;

    void updateMainProject(UUID projectId) throws BusinessException;

    void deleteProject(UUID projectId) throws BusinessException;

    void updateProjectView(UUID projectId);
}
