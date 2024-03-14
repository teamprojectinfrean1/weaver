package com.task.weaver.domain.project.dto.response;

import com.task.weaver.domain.project.entity.Project;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseGetProjectList {
    private UUID projectId;
    private String name;
    private Boolean isMainProject;

    public ResponseGetProjectList(Project project){
        this.projectId = project.getProjectId();
        this.name = project.getName();
    }
}
