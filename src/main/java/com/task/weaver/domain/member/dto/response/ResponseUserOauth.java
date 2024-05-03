package com.task.weaver.domain.member.dto.response;

import com.task.weaver.domain.userOauthMember.UserOauthMember;
import java.net.URL;
import java.util.List;
import java.util.UUID;
import lombok.Getter;

public class ResponseUserOauth {

    @Getter
    public static class MemberDTO {
        private final boolean isWeaver;
        private final UUID memberUuid;
        private final UUID userUuid;
        private final String nickname;
        private final URL profileUrl;
        private final String password;

        private MemberDTO(UserOauthMember member) {
            this.isWeaver = member.isWeaver();
            this.memberUuid = member.getMemberUuid();
            this.userUuid = member.getUuid();
            this.nickname = member.getNickname();
            this.profileUrl = member.getProfileImage();
            this.password = member.getPassword();
        }

        public static MemberDTO create(UserOauthMember members) {
            return new MemberDTO(members);
        }
    }

    @Getter
    public static class AllMember {

        private final List<MemberDTO> members;

        private AllMember(List<MemberDTO> members) {
            this.members = members;
        }

        public static AllMember create(List<MemberDTO> members) {
            return new AllMember(members);
        }
    }
}
