package com.task.weaver.domain.task.dto.request;

import java.util.Optional;
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
    private UUID updaterUuid;
    private String taskTitle;
    private String taskContent;
    private Optional<LocalDate> startDate;
    private Optional<LocalDate> endDate;
    private List<String> taskTagList;
    private String editDeletePermission;
}