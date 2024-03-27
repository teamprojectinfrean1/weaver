package com.task.weaver.domain.user.dto.response;

import com.task.weaver.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseUserIdNickname {
    private String id;
    private String userNickname;

    ResponseUserIdNickname(User user) {
        this.id = user.getId();
        this.userNickname = user.getNickname();
    }
}
