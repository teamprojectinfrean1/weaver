package com.task.weaver.domain.task.dto.response;

import com.task.weaver.domain.project.entity.Project;
import com.task.weaver.domain.status.entity.Status;
import com.task.weaver.domain.task.entity.Task;
import com.task.weaver.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseTask {

    private UUID taskId;
    private Project project;
    private Status statusTag;
    private User user;
    private String title;
    private String content;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public ResponseTask(Task task) {
        this.taskId = task.getTaskId();
        this.project = task.getProject();
        this.statusTag = task.getStatusTag();
        this.user = task.getUser();
        this.title = task.getTaskTitle();
        this.content = task.getTaskContent();
        this.startDate = task.getStartDate();
        this.endDate = task.getEndDate();
    }
}