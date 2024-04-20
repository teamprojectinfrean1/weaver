package com.task.weaver.domain.userOauthMember.util;

import com.task.weaver.domain.userOauthMember.UserOauthMember;
import com.task.weaver.domain.userOauthMember.oauth.entity.OauthUser;
import com.task.weaver.domain.userOauthMember.oauth.repository.OauthMemberRepository;
import com.task.weaver.domain.userOauthMember.user.entity.User;
import com.task.weaver.domain.userOauthMember.user.repository.UserRepository;
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
            return;
        }
        oauthMemberRepository.save((OauthUser) source);
    }
}
