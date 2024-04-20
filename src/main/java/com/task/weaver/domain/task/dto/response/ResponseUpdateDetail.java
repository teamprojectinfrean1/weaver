package com.task.weaver.domain.task.dto.response;

import com.task.weaver.domain.userOauthMember.user.entity.User;
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
public class ResponseUpdateDetail {
    private UUID memberUuid;
    private String userNickname;
    private LocalDateTime updatedDate;

    public ResponseUpdateDetail(User modifier, LocalDateTime modDate){
        this.memberUuid = modifier.getUserId();
        this.userNickname = modifier.getNickname();
        this.updatedDate = modDate;
    }
}
