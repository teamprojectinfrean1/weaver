package com.task.weaver.domain.oauth.service;

import com.task.weaver.domain.oauth.authcode.AuthCodeRequestUrlProviderComposite;
import com.task.weaver.domain.oauth.client.OauthMemberClientComposite;
import com.task.weaver.common.model.OauthServerType;
import com.task.weaver.domain.oauth.entity.OauthMember;
import com.task.weaver.domain.oauth.repository.OauthMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OauthService {

    private final AuthCodeRequestUrlProviderComposite authCodeRequestUrlProviderComposite;
    private final OauthMemberClientComposite oauthMemberClientComposite;
    private final OauthMemberRepository oauthMemberRepository;

    /**
     *
     * @param oauthServerType
     * @return oauthServerType에 해당하는 인증 서버에서 Auth Code를 받아오기 위한 URL 주소 생성
     */
    public String getAuthCodeRequestUrl(OauthServerType oauthServerType) {
        return authCodeRequestUrlProviderComposite.provide(oauthServerType);
    }

    public Long login(OauthServerType oauthServerType, String authCode) {
        OauthMember oauthMember = oauthMemberClientComposite.fetch(oauthServerType, authCode);
        OauthMember saved = oauthMemberRepository.findByOauthId(oauthMember.oauthId())
                .orElseGet(() -> oauthMemberRepository.save(oauthMember));
        return saved.id();
    }
}
