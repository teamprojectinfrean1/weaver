package com.task.weaver.domain.useroauthmember.factory;

import com.task.weaver.domain.member.Member;
import com.task.weaver.domain.member.oauth.entity.OauthMember;
import com.task.weaver.domain.useroauthmember.entity.UserOauthMember;

public interface UserOauthMemberFactory {
    UserOauthMember createUserOauthMember(Member oauthMember);
}
