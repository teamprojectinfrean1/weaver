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

    private Long project_id;
//    private StatusTag statusTag;
//    private User user;
    private Long creator_id;
    private String title;
    private String content;
    private LocalDateTime start_date;
    private LocalDateTime end_date;

    public Task toEntity(User user, Project project) {
        return Task.builder()
                .project(project)
                .user(user)
                .title(title)
                .content(content)
                .start_date(start_date)
                .end_date(end_date)
                .build();
    }

}