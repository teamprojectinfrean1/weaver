package com.task.weaver.domain.oauth.client;

import com.task.weaver.common.model.OauthServerType;
import com.task.weaver.domain.oauth.kakao.entity.OauthMember;

public interface OauthMemberClient {

    OauthServerType supportServer();

    OauthMember fetch(String code);
}
