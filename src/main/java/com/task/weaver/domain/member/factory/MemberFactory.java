package com.task.weaver.domain.member.factory;

import com.task.weaver.domain.member.entity.Member;
import com.task.weaver.domain.userOauthMember.UserOauthMember;

public interface MemberFactory {
    Member createUserOauthMember(UserOauthMember oauthUserOauthMember);
}
