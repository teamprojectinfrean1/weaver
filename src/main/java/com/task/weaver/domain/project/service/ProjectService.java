package com.task.weaver.domain.project.service;

import com.task.weaver.common.exception.BusinessException;
import com.task.weaver.domain.project.dto.request.RequestCreateProject;
import com.task.weaver.domain.project.dto.request.RequestPageProject;
import com.task.weaver.domain.project.dto.response.ResponseGetProject;
import com.task.weaver.domain.project.dto.response.ResponsePageResult;
import com.task.weaver.domain.project.entity.Project;

public interface ProjectService {

    default Project dtoToEntity(RequestCreateProject dto) {
        return Project.builder()
                .customUrl(dto.customUrl())
                .bannerUrl(dto.bannerUrl())
                .name(dto.name())
                .detail(dto.detail())
                .start_date(dto.start_date())
                .end_date(dto.end_date())
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
                .start_date(entity.getStart_date())
                .end_date(entity.getEnd_date())
                .created(entity.getCreated())
                .hasPublic(entity.getIsPublic())
                .build();
    }

    ResponsePageResult<RequestCreateProject, Project> getProjects(RequestPageProject requestPageProject)
            throws BusinessException;

    ResponseGetProject getProject(Long projectId) throws BusinessException;

    Long addProject(RequestCreateProject dto) throws BusinessException;

    void updateProject(RequestCreateProject dto) throws BusinessException;

    void deleteProject(Long projectId) throws BusinessException;

    void updateProjectView(Long projectId);
}
