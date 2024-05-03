package com.task.weaver.domain.task.dto.request;

import com.task.weaver.domain.member.entity.Member;
import com.task.weaver.domain.project.entity.Project;
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
public class RequestCreateTask {

    private UUID projectId;
    private UUID writerUuid;
    private String taskTitle;
    private String taskContent;
    private List<String> taskTagList;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String editDeletePermission;

    public Task toEntity(Member member, Project projects, List<String> taskTagList) {
        return Task.builder()
                .project(projects)
                .member(member)
                .modifier(member)
                .taskTitle(taskTitle)
                .taskContent(taskContent)
                .tags(String.join(",", taskTagList))
                .startDate(startDate)
                .endDate(endDate)
                .editDeletePermission(editDeletePermission)
                .status("진행 중")
                .build();
    }
}