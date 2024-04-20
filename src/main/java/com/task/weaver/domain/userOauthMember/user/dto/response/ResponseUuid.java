package com.task.weaver.domain.userOauthMember.user.dto.response;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ResponseUuid {

    private UUID uuid;
}
