package com.task.weaver.domain.task.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseUpdateDetail {
    private String userUuid;
    private String userNickname;
    private LocalDateTime updatedDate;
}
