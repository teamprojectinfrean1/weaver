package com.task.weaver.domain.project.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import com.task.weaver.common.exception.project.ProjectNotFoundException;
import com.task.weaver.domain.project.dto.request.RequestCreateProject;
import com.task.weaver.domain.project.dto.request.RequestPageProject;
import com.task.weaver.domain.project.dto.response.ResponsePageResult;
import com.task.weaver.domain.project.entity.Project;
import com.task.weaver.domain.project.repository.ProjectRepository;
import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ProjectServiceTest {

    @Autowired
    private ProjectService projectService;
    @Autowired
    private ProjectRepository projectRepository;

    @Test
    void getProjects() {
        RequestPageProject requestPageProject = RequestPageProject.builder().page(1).size(10).build();
        ResponsePageResult<RequestCreateProject, Project> projects = projectService.getProjects(requestPageProject);
        List<RequestCreateProject> dtoList = projects.getDtoList();

        assertThat(dtoList)
                .hasSize(10)
                .extracting("projectId")
                .containsExactlyInAnyOrder(
                        99L, 98L, 97L, 96L, 95L, 94L, 93L, 92L, 91L, 90L
                );
    }

    @Test
    void getProject() {
        RequestCreateProject project = projectService.getProject(99L);
        assertThat(project).isNotNull();
        assertThat(project.projectId()).isEqualTo(99L);
    }

    @Test
    void addProject() {
        RequestCreateProject dto = RequestCreateProject.builder()
                .customUrl("new Custom URL")
                .bannerUrl("new Banner URL")
                .name("new Project Name")
                .detail("new Project Detail")
                .hasPublic(true)
                .created(LocalDateTime.now())
                .dueDate(LocalDateTime.now().plusWeeks(1))
                .build();

        projectService.addProject(dto);

        RequestCreateProject project = projectService.getProject(101L);

        assertThat(project).isNotNull();
        assertThat(project.name()).isEqualTo("new Project Name");
    }

    @Test
    void updateProject() {
        Optional<Project> result = projectRepository.findById(101L);
        Project project = result.get();
        project.changeName("update Project");
        project.changeDetail("update Detail");
        project.changePublic();
        RequestCreateProject requestCreateProject = projectService.entityToDto(project);
        projectService.updateProject(requestCreateProject);
    }

    @Test
    void deleteProject() {
//        projectService.deleteProject(99L);
        assertThatThrownBy(() -> projectService.getProject(99L))
                .isInstanceOf(ProjectNotFoundException.class)
                .hasMessage("해당 프로젝트를 찾을 수 없습니다.");
    }
}