package com.task.weaver.domain.userOauthMember.user.dto.response;

import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResponseUuid {

    private UUID memberUuid;
    private UUID userUuid;

    public static ResponseUuid of(UUID memberUuid, UUID userUuid) {
        return ResponseUuid.builder()
                .memberUuid(memberUuid)
                .userUuid(userUuid)
                .build();
    }
}
