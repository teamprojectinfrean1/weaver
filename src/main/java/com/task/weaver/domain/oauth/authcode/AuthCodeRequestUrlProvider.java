package com.task.weaver.domain.oauth.authcode;

import com.task.weaver.common.model.OauthServerType;

/**
 *  AuthCode를 발급할 URL을 제공하는 기능을 제공합니다.
 */
public interface AuthCodeRequestUrlProvider {

    OauthServerType supportServer();

    String provide();
}
