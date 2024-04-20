package com.task.weaver.domain.member.user.dto.response;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseGetUserList {
    private UUID memberId;
    private String userNickname;
    private String profileImage;
}
