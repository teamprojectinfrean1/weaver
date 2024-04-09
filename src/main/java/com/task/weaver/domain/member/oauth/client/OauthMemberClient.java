package com.task.weaver.domain.member.oauth.client;

import com.task.weaver.common.model.OauthServerType;
import com.task.weaver.domain.member.oauth.entity.OauthMember;

/**
 * Oauth Member 생성 로직
 */
public interface OauthMemberClient {

    OauthServerType supportServer();

    /**
     * @param code  Auth Code를 통해 AccessToken 발급 -> <br/>
     *             -> AccessToken을 통해 회원 정보 조회 -> OauthMember 생성
     * @return OauthMember
     */
    OauthMember fetch(String code);
}
