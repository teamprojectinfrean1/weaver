package com.task.weaver.domain.useroauthmember.dto.response;

import com.task.weaver.domain.member.oauth.entity.OauthMember;
import com.task.weaver.domain.member.user.entity.User;
import java.util.List;
import lombok.Getter;

public class ResponseUserOauth {

    @Getter
    public static class AllMember {
        private List<User> users;
        private List<OauthMember> members;

        private AllMember(List<User> users, List<OauthMember> members) {
            this.users = users;
            this.members = members;
        }

        public static AllMember create(List<User> users, List<OauthMember> members) {
            return new AllMember(users, members);
        }
    }
}
