package com.task.weaver.domain.task.dto.response;

import com.task.weaver.domain.issue.dto.request.RequestIssueForTask;
import com.task.weaver.domain.task.entity.Task;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseGetTask {
    private UUID taskId;
    private String taskTitle;
    private String taskContent;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private ResponseUpdateDetail lastUpdateDetail;
    private String taskTags;
    private String editDeletePermission;
    private String taskStatus;

    private List<RequestIssueForTask> issueList;

    public ResponseGetTask(Task task, List<RequestIssueForTask> requestIssueForTask){
        this.taskId = task.getTaskId();
        this.taskTitle = task.getTaskTitle();
        this.taskContent = task.getTaskContent();
        this.taskTags = task.getTags();
        this.startDate = task.getStartDate();
        this.endDate = task.getEndDate();
        this.editDeletePermission = task.getEditDeletePermission();
        this.taskStatus = task.getStatus();
        this.issueList = requestIssueForTask;
    }
}
