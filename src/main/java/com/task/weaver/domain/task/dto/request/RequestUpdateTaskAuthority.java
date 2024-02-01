package com.task.weaver.domain.task.dto.request;

import com.task.weaver.domain.project.entity.Project;
import com.task.weaver.domain.task.entity.Task;
import com.task.weaver.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestUpdateTaskAuthority {
    private Project project;
    private User user;
    private Task task;
    private String code;
}
