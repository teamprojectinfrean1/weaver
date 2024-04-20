package com.task.weaver.domain.member.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestUpdateUser {
    private String type;
    private Object value;

    @Data
    public static class PasswordUpdate {
        private String currentPassword;
        private String updatePassword;
    }
}
