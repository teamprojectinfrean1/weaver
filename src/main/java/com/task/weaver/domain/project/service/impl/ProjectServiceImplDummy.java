package com.task.weaver.domain.project.service.impl;

import com.task.weaver.common.exception.BusinessException;
import com.task.weaver.common.exception.project.ProjectNotFoundException;
import com.task.weaver.domain.project.dto.request.RequestCreateProject;
import com.task.weaver.domain.project.dto.request.RequestPageProject;
import com.task.weaver.domain.project.dto.response.ResponsePageResult;
import com.task.weaver.domain.project.entity.Project;
import com.task.weaver.domain.project.repository.ProjectRepository;
import com.task.weaver.domain.project.service.ProjectService;
import java.util.Optional;
import java.util.function.Function;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProjectServiceImplDummy implements ProjectService {

    private final ProjectRepository projectRepository;

    @Override
    public ResponsePageResult<RequestCreateProject, Project> getProjects(final RequestPageProject requestPageProject,
                                                                         final Long userId) throws BusinessException {

        Pageable pageable = requestPageProject.getPageable(Sort.by("projectId").descending());
        Page<Project> result = projectRepository.findAll(pageable);
        Function<Project, RequestCreateProject> fn = (this::entityToDto);

        return new ResponsePageResult<>(result, fn);
    }

    @Override
    public RequestCreateProject getProject(final Long projectId) throws BusinessException {
        return projectRepository.findById(projectId)
                .map(this::entityToDto)
                .orElseThrow(() -> new ProjectNotFoundException(new Throwable(String.valueOf(projectId))));
    }

    @Override
    public Long addProject(final RequestCreateProject dto) throws BusinessException {
        Project project = dtoToEntity(dto);
        projectRepository.save(project);
        return project.getProjectId();
    }

    @Override
    public void updateProject(final RequestCreateProject dto) throws BusinessException {
        Optional<Project> result = projectRepository.findById(dto.getProjectId());

        if (result.isPresent()) {
            Project entity = result.get();
            entity.changeDetail(dto.detail());
            entity.changeName(dto.name());
            entity.changePublic();
            projectRepository.save(entity);
        }
        throw new ProjectNotFoundException(new Throwable(String.valueOf(dto.getProjectId())));
    }

    @Override
    public void deleteProject(final Long projectId) throws BusinessException {
        Optional<Project> project = projectRepository.findById(projectId);
        if (project.isPresent()){
            projectRepository.deleteById(projectId);
            return;
        }
        throw new ProjectNotFoundException(new Throwable(String.valueOf(projectId)));
    }
}
