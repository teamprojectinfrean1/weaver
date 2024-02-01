package com.task.weaver.domain.user.dto.request;

import com.task.weaver.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestCreateUser {
    private String nickname;
    private String mail;
    private String password;

    public User toEntity(){
        return User.builder()
                .name(nickname)
                .mail(mail)
                .password(password)
                .build();
    }
}
