package com.task.weaver.domain.task.dto.request;

public record RequestCreateTaskAuthority(Long taskAuthorityId,
                                         Long projectId,
                                         Long userId,
                                         Long taskId,
                                         String code) {
}
