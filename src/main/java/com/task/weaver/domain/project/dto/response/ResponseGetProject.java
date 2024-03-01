package com.task.weaver.domain.project.dto.response;

import com.task.weaver.domain.issue.dto.response.IssueResponse;
import com.task.weaver.domain.project.entity.Project;
import com.task.weaver.domain.task.dto.response.ResponseTask;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseGetProject {
    private Long project_id;
    private String name;
    private String detail;
    private LocalDateTime start_date;
    private LocalDateTime end_date;
    private LocalDateTime created;
    private String customUrl;
    private String bannerUrl;
    private List<ResponseTask> taskList;
    private List<IssueResponse> issueList;

    public ResponseGetProject(Project project){
        this.project_id = project.getProjectId();
        this.name = project.getName();
        this.detail = project.getDetail();
        this.start_date = project.getStart_date();
        this.end_date = project.getEnd_date();
        this.created = project.getCreated();
        this.customUrl = project.getCustomUrl();
        this.bannerUrl = project.getBannerUrl();
    }
}
