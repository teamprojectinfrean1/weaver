package com.task.weaver.domain.userOauthMember.oauth.client;

import com.task.weaver.common.model.OauthServerType;
import com.task.weaver.domain.userOauthMember.oauth.entity.OauthUser;

/**
 * Oauth UserOauthMember 생성 로직
 */
public interface OauthMemberClient {

    OauthServerType supportServer();

    /**
     * @param code  Auth Code를 통해 AccessToken 발급 -> <br/>
     *             -> AccessToken을 통해 회원 정보 조회 -> OauthUser 생성
     * @return OauthUser
     */
    OauthUser fetch(String code);
}
