package com.task.weaver.domain.member.dto.response;

import com.task.weaver.domain.userOauthMember.oauth.entity.OauthUser;
import com.task.weaver.domain.userOauthMember.user.entity.User;
import java.util.List;
import lombok.Getter;

public class ResponseUserOauth {

    @Getter
    public static class AllMember {
        private List<User> users;
        private List<OauthUser> members;

        private AllMember(List<User> users, List<OauthUser> members) {
            this.users = users;
            this.members = members;
        }

        public static AllMember create(List<User> users, List<OauthUser> members) {
            return new AllMember(users, members);
        }
    }
}
