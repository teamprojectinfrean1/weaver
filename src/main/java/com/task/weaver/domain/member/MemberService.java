package com.task.weaver.domain.member;

import com.task.weaver.domain.member.oauth.entity.OauthMember;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class MemberService {
    
    private final MemberRepository memberRepository;

    @Transactional
    public Member getOAuthMember(final OauthMember oauthMember) {
        return memberRepository.findByOauth(oauthMember)
                .orElseGet(() -> memberRepository.save(
                        Member.builder()
                        .loginType(LoginType.OAUTH)
                        .oauth(oauthMember)
                        .build()));
    }
}
