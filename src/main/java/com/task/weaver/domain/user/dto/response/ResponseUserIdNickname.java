package com.task.weaver.domain.user.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ResponseUserIdNickname {

    private UUID uuid;
    private String id;
    private String nickname;
    @JsonProperty("isSuccess")
    private boolean isSuccess;
}
