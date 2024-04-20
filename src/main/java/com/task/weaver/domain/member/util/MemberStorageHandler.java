package com.task.weaver.domain.member.util;

import com.task.weaver.common.exception.ErrorCode;
import com.task.weaver.common.exception.member.MemberCannotConvertedException;
import com.task.weaver.domain.member.UserOauthMember;
import com.task.weaver.domain.member.oauth.entity.OauthUser;
import com.task.weaver.domain.member.oauth.repository.OauthMemberRepository;
import com.task.weaver.domain.member.user.entity.User;
import com.task.weaver.domain.member.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class MemberStorageHandler {

    private final UserRepository userRepository;
    private final OauthMemberRepository oauthMemberRepository;

    public void handle(final UserOauthMember source) {
        if (source instanceof User){
            userRepository.save((User) source);
        } else if (source instanceof OauthUser) {
            oauthMemberRepository.save((OauthUser) source);
        } else {
            throw new MemberCannotConvertedException(ErrorCode.MEMBER_CANNOT_CONVERTED, "멤버 변환 실패");
        }
    }
}