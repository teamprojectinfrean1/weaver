package com.task.weaver.domain.user.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ResponseUuid {

    private UUID uuid;
    @JsonProperty("isSuccess")
    private boolean isSuccess;
}
