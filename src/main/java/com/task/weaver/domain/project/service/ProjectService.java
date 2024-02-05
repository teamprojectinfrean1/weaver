package com.task.weaver.domain.project.service;

import com.task.weaver.common.exception.BusinessException;
import com.task.weaver.domain.project.dto.request.RequestCreateProject;
import com.task.weaver.domain.project.dto.request.RequestPageProject;
import com.task.weaver.domain.project.dto.response.ResponsePageResult;
import com.task.weaver.domain.project.entity.Project;

public interface ProjectService {

    default Project dtoToEntity(RequestCreateProject dto) {
        return Project.builder()
                .customUrl(dto.customUrl())
                .bannerUrl(dto.bannerUrl())
                .name(dto.name())
                .detail(dto.detail())
                .dueDate(dto.dueDate())
                .created(dto.created())
                .isPublic(dto.hasPublic())
                .build();
    }

    default RequestCreateProject entityToDto(Project entity) {
        return RequestCreateProject.builder()
                .projectId(entity.getProjectId())
                .customUrl(entity.getCustomUrl())
                .bannerUrl(entity.getBannerUrl())
                .name(entity.getName())
                .detail(entity.getDetail())
                .dueDate(entity.getDueDate())
                .created(entity.getCreated())
                .hasPublic(entity.getIsPublic())
                .build();
    }

    ResponsePageResult<RequestCreateProject, Project> getProjects(RequestPageProject requestPageProject)
            throws BusinessException;

    RequestCreateProject getProject(Long projectId) throws BusinessException;

    Long addProject(RequestCreateProject dto) throws BusinessException;

    void updateProject(RequestCreateProject dto) throws BusinessException;

    void deleteProject(Long projectId) throws BusinessException;

    void updateProjectView(Long projectId) throws BusinessException;
}
