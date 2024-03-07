package com.task.weaver.domain.task.dto.response;

import com.task.weaver.domain.issue.dto.response.ResponseIssueForTask;
import com.task.weaver.domain.task.entity.Task;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseGetTask {
    private Long taskId;
    private String taskTitle;
    private String taskContent;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private ResponseUpdateDetail lastUpdateDetail;
    private String lastWriterId;
    private LocalDateTime lastUpdateDate;
    private List<ResponseIssueForTask> issueList;
    private String editDeletePermission;
    private String taskStatus;
    public ResponseGetTask(Task task){
        this.taskId = task.getTaskId();
        this.taskTitle = task.getTaskTitle();
        this.taskContent = task.getTaskContent();
        this.startDate = task.getStartDate();
        this.endDate = task.getEndDate();
    }
}
