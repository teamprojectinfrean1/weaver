package com.task.weaver.domain.authorization.factory.impl;

import com.task.weaver.domain.authorization.entity.UserOauthMember;
import com.task.weaver.domain.authorization.factory.UserOauthMemberFactory;
import com.task.weaver.domain.authorization.repository.UserOauthMemberRepository;
import com.task.weaver.domain.member.LoginType;
import com.task.weaver.domain.member.Member;
import com.task.weaver.domain.member.oauth.entity.OauthMember;
import com.task.weaver.domain.member.oauth.repository.OauthMemberRepository;
import com.task.weaver.domain.member.user.entity.User;
import com.task.weaver.domain.member.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserOauthMemberFactoryImpl implements UserOauthMemberFactory {

    private final UserOauthMemberRepository userOauthMemberRepository;
    private final UserRepository userRepository;
    private final OauthMemberRepository oauthMemberRepository;

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

//    private UserOauthMember allocationOauthAgent(final OauthMember member, final UserOauthMember normalUser) {
//        member.agent(normalUser);
//        oauthMemberRepository.save(member);
//        return member.getUserOauthMember();
//    }

//    private UserOauthMember allocationUserAgent(final User member, final UserOauthMember normalUser) {
//        member.agent(normalUser);
//        userRepository.save(member);
//        return member.getUserOauthMember();
//    }
}
