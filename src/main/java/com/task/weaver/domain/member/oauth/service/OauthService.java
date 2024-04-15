package com.task.weaver.domain.member.oauth.service;

import com.task.weaver.common.jwt.provider.JwtTokenProvider;
import com.task.weaver.common.redis.RefreshToken;
import com.task.weaver.common.redis.RefreshTokenRepository;
import com.task.weaver.domain.authorization.dto.response.ResponseToken;
import com.task.weaver.domain.authorization.entity.UserOauthMember;
import com.task.weaver.domain.member.oauth.authcode.AuthCodeRequestUrlProviderComposite;
import com.task.weaver.domain.member.oauth.client.OauthMemberClientComposite;
import com.task.weaver.domain.member.oauth.entity.OauthMember;
import com.task.weaver.common.model.OauthServerType;
import com.task.weaver.domain.member.oauth.repository.OauthMemberRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OauthService {

    private final AuthCodeRequestUrlProviderComposite authCodeRequestUrlProviderComposite;
    private final OauthMemberClientComposite oauthMemberClientComposite;
    private final OauthMemberRepository oauthMemberRepository;

    /**
     * @param oauthServerType
     * @return oauthServerType에 해당하는 인증 서버에서 Auth Code를 받아오기 위한 URL 주소 생성
     */
    public String getAuthCodeRequestUrl(OauthServerType oauthServerType) {
        return authCodeRequestUrlProviderComposite.provide(oauthServerType);
    }

    @Transactional(rollbackFor = Exception.class)
    public OauthMember login(OauthServerType oauthServerType, String authCode) {
        OauthMember oauthMember = oauthMemberClientComposite.fetch(oauthServerType, authCode);
        return oauthMemberRepository.findByOauthId(oauthMember.oauthId())
                .orElseGet(() -> oauthMemberRepository.save(oauthMember));
    }

//    public void updateAgent(final OauthMember oauthMember, final UserOauthMember userOauthMember) {
//        oauthMember.agent(userOauthMember);
//        oauthMemberRepository.save(oauthMember);
//    }
}
