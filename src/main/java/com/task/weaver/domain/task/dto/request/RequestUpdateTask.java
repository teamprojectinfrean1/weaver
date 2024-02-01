package com.task.weaver.domain.task.dto.request;

import com.task.weaver.domain.project.entity.Project;
import com.task.weaver.domain.status.StatusTag;
import com.task.weaver.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestUpdateTask {
    private Project project;
    private StatusTag statusTag;
    private User user;
    private String taskName;
    private String detail;
    private LocalDate dueDate;
}