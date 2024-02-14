package com.task.weaver.domain.user.dto.response;

import com.task.weaver.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
public class ResponseUser {
    private Long user_id;
    private String name;
    private String email;
    private String password;

    public ResponseUser(User user){
        this.user_id = user.getUserId();
        this.name = user.getName();
        this.email = user.getEmail();
    }
}
