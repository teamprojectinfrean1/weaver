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
public class ResponseGetProjectList {
    private UUID projectId;
    private String projectName;
    private Boolean isMainProject;
    private Permission permission;
    private URL projectImage;

    public ResponseGetProjectList(Project project, Permission permission, UUID mainProjectId) {
        this.projectId = project.getProjectId();
        this.projectName = project.getName();
        this.isMainProject = project.getProjectId().equals(mainProjectId);
    }
}
