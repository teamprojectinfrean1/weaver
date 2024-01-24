package com.task.weaver.domain.project.service.impl;

import com.task.weaver.common.exception.BusinessException;
import com.task.weaver.common.exception.ErrorCode;
import com.task.weaver.common.exception.project.ProjectNotFoundException;
import com.task.weaver.domain.project.dto.request.RequestCreateProject;
import com.task.weaver.domain.project.dto.request.RequestPageProject;
import com.task.weaver.domain.project.dto.response.ResponsePageResult;
import com.task.weaver.domain.project.entity.Project;
import com.task.weaver.domain.project.repository.ProjectRepository;
import com.task.weaver.domain.project.service.ProjectService;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProjectServiceImplDummy implements ProjectService {

    private final ProjectRepository projectRepository;

    @Override
    public ResponsePageResult<RequestCreateProject, Project> getProjects(final RequestPageProject requestPageProject,
                                                                         final Long userId) throws BusinessException {
        return null;
    }

    @Override
    public RequestCreateProject getProject(final Long projectId) throws BusinessException {
        return projectRepository.findById(projectId)
                .map(this::entityToDto)
                .orElseThrow(() -> new ProjectNotFoundException(new Throwable(String.valueOf(projectId))));
    }

    @Override
    public Long addProject(final RequestCreateProject dto) throws BusinessException {
        return null;
    }

    @Override
    public void updateProject(final RequestCreateProject dto) throws BusinessException {

    }

    @Override
    public void deleteProject(final Long projectId) throws BusinessException {

    }
}
