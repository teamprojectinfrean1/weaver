package com.task.weaver.domain.useroauthmember.factory.impl;

import com.task.weaver.domain.member.LoginType;
import com.task.weaver.domain.member.Member;
import com.task.weaver.domain.member.oauth.entity.OauthMember;
import com.task.weaver.domain.member.user.entity.User;
import com.task.weaver.domain.useroauthmember.entity.UserOauthMember;
import com.task.weaver.domain.useroauthmember.factory.UserOauthMemberFactory;
import com.task.weaver.domain.useroauthmember.repository.UserOauthMemberRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserOauthMemberFactoryImpl implements UserOauthMemberFactory {

    private final UserOauthMemberRepository userOauthMemberRepository;

    @Override
    public UserOauthMember createUserOauthMember(final Member member) {
        if (member.isWeaver()) {
            return createNormalUser((User) member);
        }
        return createOauthUser((OauthMember) member);
    }

    private UserOauthMember createNormalUser(final User user) {
        UserOauthMember userOauthMember = UserOauthMember.builder()
                .loginType(LoginType.WEAVER)
                .user(user)
                .isOnline(true)
                .build();
        return userOauthMemberRepository.findByUser(user)
                .orElseGet(() -> userOauthMemberRepository.save(userOauthMember));
    }

    private UserOauthMember createOauthUser(final OauthMember oauthMember) {
        UserOauthMember userOauthMember = UserOauthMember.builder()
                .loginType(LoginType.OAUTH)
                .oauthMember(oauthMember)
                .isOnline(true)
                .build();
        return userOauthMemberRepository.findByOauthMember(oauthMember)
                .orElseGet(() -> userOauthMemberRepository.save(userOauthMember));
    }
}
