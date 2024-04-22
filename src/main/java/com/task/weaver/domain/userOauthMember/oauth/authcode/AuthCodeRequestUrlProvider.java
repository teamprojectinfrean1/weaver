package com.task.weaver.domain.userOauthMember.oauth.authcode;

import com.task.weaver.common.model.OauthServerType;

/**
 *  AuthCode를 발급할 URL을 제공하는 기능을 제공합니다.
 *  추상화를 통해 다양한 OAuth 서버를 지원합니다.
 */
public interface AuthCodeRequestUrlProvider {

    /**
     *
     * @return 자신이 지원할 수 있는 OauthServerType을 나타냅니다.
     */
    OauthServerType supportServer();

    /**
     * @return URL을 생성하여 반환하며, <br>
     * OAuth 서버에서 요청하는 항목을 QueryParam 형태로 담아 해당 주소로 Redirect 시킵니다.
     */
    String provide();
}
