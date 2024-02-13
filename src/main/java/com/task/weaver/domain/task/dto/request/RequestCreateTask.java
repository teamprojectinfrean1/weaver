package com.task.weaver.domain.task.dto.request;

import com.task.weaver.domain.project.entity.Project;
import com.task.weaver.domain.status.entity.StatusTag;
import com.task.weaver.domain.task.entity.Task;
import com.task.weaver.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestCreateTask {

    private Project project;
    private StatusTag statusTag;
    private User user;
    private String taskName;
    private String detail;
    private LocalDateTime dueDate;

    public Task toEntity() {
        return Task.builder()
                .project(project)
                .statusTag(statusTag)
                .user(user)
                .taskName(taskName)
                .detail(detail)
                .dueDate(dueDate)
                .build();
    }

}