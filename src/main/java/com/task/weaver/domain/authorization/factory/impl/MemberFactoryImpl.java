package com.task.weaver.domain.authorization.factory.impl;

import com.task.weaver.domain.authorization.entity.Member;
import com.task.weaver.domain.authorization.factory.MemberFactory;
import com.task.weaver.domain.authorization.repository.MemberRepository;
import com.task.weaver.domain.member.LoginType;
import com.task.weaver.domain.member.UserOauthMember;
import com.task.weaver.domain.member.oauth.entity.OauthUser;
import com.task.weaver.domain.member.oauth.repository.OauthMemberRepository;
import com.task.weaver.domain.member.user.entity.User;
import com.task.weaver.domain.member.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MemberFactoryImpl implements MemberFactory {

    private final MemberRepository userOauthMemberRepository;
    private final UserRepository userRepository;
    private final OauthMemberRepository oauthMemberRepository;

    @Override
    public Member createUserOauthMember(final UserOauthMember userOauthMember) {
        if (userOauthMember.isWeaver()) {
            return createNormalUser((User) userOauthMember);
        }
        return createOauthUser((OauthUser) userOauthMember);
    }

    private Member createNormalUser(final User user) {
        Member member = Member.builder()
                .loginType(LoginType.WEAVER)
                .user(user)
                .isOnline(true)
                .build();
        return userOauthMemberRepository.findByUser(user)
                .orElseGet(() -> userOauthMemberRepository.save(member));
    }

    private Member createOauthUser(final OauthUser oauthMember) {
        Member member = Member.builder()
                .loginType(LoginType.OAUTH)
                .oauthMember(oauthMember)
                .isOnline(true)
                .build();
        return userOauthMemberRepository.findByOauthMember(oauthMember)
                .orElseGet(() -> userOauthMemberRepository.save(member));
    }

//    private UserOauthMember allocationOauthAgent(final OauthUser member, final UserOauthMember normalUser) {
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
