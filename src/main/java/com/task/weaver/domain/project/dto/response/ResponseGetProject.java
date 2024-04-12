package com.task.weaver.domain.project.dto.response;

import com.task.weaver.domain.project.entity.Project;
import com.task.weaver.domain.task.dto.response.ResponseUpdateDetail;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseGetProject {
    private UUID projectId;
    private String projectName;
    private String projectContent;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private List<String> projectTagList;
    private ResponseUpdateDetail lastUpdateDetail;

    public ResponseGetProject(Project project){
        this.projectId = project.getProjectId();
        this.projectName = project.getName();
        this.projectContent = project.getDetail();
        this.startDate = project.getStartDate();
        this.endDate = project.getEndDate();
    }
}
