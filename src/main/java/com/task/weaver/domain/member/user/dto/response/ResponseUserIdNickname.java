package com.task.weaver.domain.member.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ResponseUserIdNickname {

    private String id;
    private String nickname;
}
