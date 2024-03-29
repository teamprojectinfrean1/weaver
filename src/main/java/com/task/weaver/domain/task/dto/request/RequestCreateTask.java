package com.task.weaver.domain.task.dto.request;

import com.task.weaver.domain.issue.dto.request.RequestIssueForTask;
import com.task.weaver.domain.issue.dto.response.ResponseIssueForTask;
import com.task.weaver.domain.project.entity.Project;
import com.task.weaver.domain.status.entity.Status;
import com.task.weaver.domain.task.entity.Task;
import com.task.weaver.domain.user.entity.User;
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
public class RequestCreateTask {

    private UUID projectId;
//    private StatusTag statusTag;
//    private User user;
    private UUID writerUuid;
    private String taskTitle;
    private String taskContent;
    private List<String> taskTagList;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
//    private List<RequestIssueForTask> issueList;
    private String editDeletePermission;

    public Task toEntity(User user, Project project) {
        return Task.builder()
                .project(project)
                .user(user)
                .modifier(user)
                .taskTitle(taskTitle)
                .taskContent(taskContent)
                .startDate(startDate)
                .endDate(endDate)
                .editDeletePermission(editDeletePermission)
                .status("진행 중")
                .build();
    }

}