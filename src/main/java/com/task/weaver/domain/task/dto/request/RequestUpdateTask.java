package com.task.weaver.domain.task.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestUpdateTask {
//    private Project project;
//    private StatusTag statusTag;
//    private User user;
    private UUID updaterUuid;
    private String taskTitle;
    private String taskContent;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<String> taskTagList;
    private String editDeletePermission;
}