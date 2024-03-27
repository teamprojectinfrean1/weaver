package com.task.weaver.domain.user.dto.response;

import com.task.weaver.domain.user.entity.User;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseUserIdNickname {
    private UUID uuid;
    private String id;
    private String userNickname;
    private boolean data;

    ResponseUserIdNickname(User user, boolean data) {
        this.uuid = user.getUserId();
        this.id = user.getId();
        this.userNickname = user.getNickname();
        this.data = data;
    }
}
