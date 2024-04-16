package com.task.weaver.domain.authorization.factory;

import com.task.weaver.domain.authorization.entity.Member;
import com.task.weaver.domain.member.UserOauthMember;

public interface MemberFactory {
    Member createUserOauthMember(UserOauthMember oauthUserOauthMember);
}
