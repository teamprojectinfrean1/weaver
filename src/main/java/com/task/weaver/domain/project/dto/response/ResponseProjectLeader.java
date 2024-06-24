package com.task.weaver.domain.project.dto.response;

import com.task.weaver.domain.member.entity.Member;
import java.net.URL;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseProjectLeader {
    private UUID leaderUUID;
    private String nickname;
    private URL profileImage;

    private ResponseProjectLeader(Member member) {
        this.leaderUUID = member.getId();
        this.nickname = member.resolveMemberByLoginType().getNickname();
        this.profileImage = member.resolveMemberByLoginType().getProfileImage();
    }

    public static ResponseProjectLeader of(Member member) {
        return new ResponseProjectLeader(member);
    }
}
