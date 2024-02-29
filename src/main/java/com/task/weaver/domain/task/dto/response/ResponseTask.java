package com.task.weaver.domain.task.dto.response;

import com.task.weaver.domain.project.entity.Project;
import com.task.weaver.domain.status.entity.StatusTag;
import com.task.weaver.domain.task.entity.Task;
import com.task.weaver.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseTask {

    private Long taskId;
    private Project project;
    private StatusTag statusTag;
    private User user;
    private String title;
    private String content;
    private LocalDateTime start_date;
    private LocalDateTime end_date;

    public ResponseTask(Task task) {
        this.taskId = task.getTaskId();
        this.project = task.getProject();
        this.statusTag = task.getStatusTag();
        this.user = task.getUser();
        this.title = task.getTitle();
        this.content = task.getContent();
        this.start_date = task.getStart_date();
        this.end_date = task.getEnd_date();
    }
}