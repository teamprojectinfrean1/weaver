package com.task.weaver.domain.task.dto.request;

public record RequestCreateTask(Long taskId,
                                Long projectId,
                                Long createUserId,
                                String taskName,
                                String body) {
}
