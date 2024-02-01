package com.task.weaver.domain.task.dto.response;


import com.task.weaver.domain.project.entity.Project;
import com.task.weaver.domain.task.entity.Task;
import com.task.weaver.domain.task.entity.TaskAuthority;
import com.task.weaver.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseTaskAuthority {
    private Long taskAuthorityId;
    private Project project;
    private User user;
    private Task task;
    private String code;

    public ResponseTaskAuthority(TaskAuthority taskAuthority) {
        this.taskAuthorityId = taskAuthority.getTaskAuthorityId();
        this.project = taskAuthority.getProject();
        this.user = taskAuthority.getUser();
        this.task = taskAuthority.getTask();
        this.code = taskAuthority.getCode();
    }
}
