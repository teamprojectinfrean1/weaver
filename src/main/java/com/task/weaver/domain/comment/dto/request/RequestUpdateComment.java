package com.task.weaver.domain.comment.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestUpdateComment {
    private UUID writerId;
    private UUID issueId;
    private String commentBody;
}