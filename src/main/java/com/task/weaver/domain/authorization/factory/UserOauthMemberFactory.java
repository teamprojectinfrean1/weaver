package com.task.weaver.domain.authorization.factory;

import com.task.weaver.domain.member.Member;
import com.task.weaver.domain.authorization.entity.UserOauthMember;

public interface UserOauthMemberFactory {
    UserOauthMember createUserOauthMember(Member oauthMember);
}
