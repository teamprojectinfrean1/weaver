package com.task.weaver.domain.task.dto.request;

import com.task.weaver.domain.issue.dto.response.ResponseIssueForTask;
import com.task.weaver.domain.project.entity.Project;
import com.task.weaver.domain.status.entity.StatusTag;
import com.task.weaver.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestUpdateTask {
//    private Project project;
//    private StatusTag statusTag;
//    private User user;
    private String taskTitle;
    private String taskContent;
    private LocalDate startDate;
    private LocalDate endDate;
    private String taskStatus;
    private List<String> taskTagList;
    private List<ResponseIssueForTask> issueList;
    private String editDeletePermission;
}