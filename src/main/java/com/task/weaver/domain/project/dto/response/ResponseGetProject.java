package com.task.weaver.domain.project.dto.response;

import com.task.weaver.domain.project.entity.Project;
import com.task.weaver.domain.task.dto.response.ResponseUpdateDetail;

import java.net.URL;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
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
    private URL projectImage;
    private String projectTags;
    private ResponseUpdateDetail lastUpdateDetail;
    private ResponseProjectLeader projectLeader;

    public ResponseGetProject(Project project, ResponseUpdateDetail responseUpdateDetail){
        this.projectId = project.getProjectId();
        this.projectName = project.getName();
        this.projectContent = project.getDetail();
        this.startDate = project.getStartDate();
        this.endDate = project.getEndDate();
        this.projectTags = project.getTags();
        this.projectImage = project.getProjectImage();
        this.lastUpdateDetail = responseUpdateDetail;
    }

    public ResponseGetProject(Project project, ResponseUpdateDetail responseUpdateDetail, ResponseProjectLeader projectLeader){
        this.projectId = project.getProjectId();
        this.projectName = project.getName();
        this.projectContent = project.getDetail();
        this.startDate = project.getStartDate();
        this.endDate = project.getEndDate();
        this.projectTags = project.getTags();
        this.projectImage = project.getProjectImage();
        this.lastUpdateDetail = responseUpdateDetail;
        this.projectLeader = projectLeader;
    }
}
