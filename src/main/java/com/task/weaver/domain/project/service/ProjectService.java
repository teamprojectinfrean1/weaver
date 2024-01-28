package com.task.weaver.domain.project.service;

import com.task.weaver.common.exception.BusinessException;
import com.task.weaver.domain.project.dto.request.RequestCreateProject;
import com.task.weaver.domain.project.dto.request.RequestPageProject;
import com.task.weaver.domain.project.dto.response.ResponsePageResult;
import com.task.weaver.domain.project.entity.Project;

public interface ProjectService {

    default Project dtoToEntity(RequestCreateProject dto) {
        return Project.builder()
                .customUrl(dto.getCustomUrl())
                .bannerUrl(dto.getBannerUrl())
                .name(dto.getName())
                .detail(dto.getDetail())
                .dueDate(dto.getDueDate())
                .created(dto.getCreated())
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

    ResponsePageResult<RequestCreateProject, Project> getProjects(RequestPageProject requestPageProject, Long userId)
            throws BusinessException;

    RequestCreateProject getProject(Long projectId) throws BusinessException;

    Long addProject(RequestCreateProject dto) throws BusinessException;

    void updateProject(RequestCreateProject dto) throws BusinessException;

    void deleteProject(Long projectId) throws BusinessException;
}
