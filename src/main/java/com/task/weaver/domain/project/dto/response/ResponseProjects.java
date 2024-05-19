package com.task.weaver.domain.project.dto.response;

import com.task.weaver.common.model.Permission;
import com.task.weaver.domain.project.entity.Project;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.URL;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseProjects {
    private UUID projectId;
    private String projectName;
    private Boolean isMainProject;
    private Permission permission;
    private URL projectImage;

    public ResponseProjects(Project project, Permission permission, UUID mainProjectId) {
        this.projectId = project.getProjectId();
        this.projectName = project.getName();
        this.permission = permission;
        this.isMainProject = project.getProjectId().equals(mainProjectId);
        this.projectImage = project.getProjectImage();
    }
}
