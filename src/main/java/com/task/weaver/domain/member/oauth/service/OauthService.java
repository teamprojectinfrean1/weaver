package com.task.weaver.domain.member.oauth.service;

import com.task.weaver.domain.member.oauth.authcode.AuthCodeRequestUrlProviderComposite;
import com.task.weaver.domain.member.oauth.client.OauthMemberClientComposite;
import com.task.weaver.domain.member.oauth.entity.OauthUser;
import com.task.weaver.common.model.OauthServerType;
import com.task.weaver.domain.member.oauth.repository.OauthMemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public OauthUser login(OauthServerType oauthServerType, String authCode) {
        OauthUser oauthMember = oauthMemberClientComposite.fetch(oauthServerType, authCode);
        return oauthMemberRepository.findByOauthId(oauthMember.oauthId())
                .orElseGet(() -> oauthMemberRepository.save(oauthMember));
    }
}
