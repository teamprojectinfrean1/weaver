package com.task.weaver.domain.member.factory.impl;

import com.task.weaver.domain.member.entity.Member;
import com.task.weaver.domain.member.factory.MemberFactory;
import com.task.weaver.domain.member.repository.MemberRepository;
import com.task.weaver.domain.userOauthMember.LoginType;
import com.task.weaver.domain.userOauthMember.UserOauthMember;
import com.task.weaver.domain.userOauthMember.oauth.entity.OauthUser;
import com.task.weaver.domain.userOauthMember.user.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MemberFactoryImpl implements MemberFactory {

    private final MemberRepository userOauthMemberRepository;

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
}
