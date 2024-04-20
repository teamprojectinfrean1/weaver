package com.task.weaver.domain.member.util;

import com.task.weaver.common.exception.ErrorCode;
import com.task.weaver.common.exception.member.MemberCannotConvertedException;
import com.task.weaver.domain.member.UserOauthMember;
import com.task.weaver.domain.member.oauth.entity.OauthUser;
import org.springframework.core.convert.converter.Converter;

public class MemberToOauthMemberConverter implements Converter<UserOauthMember, OauthUser> {
    @Override
    public OauthUser convert(final UserOauthMember source) {
        if (source instanceof OauthUser){
            return (OauthUser) source;
        }
        throw new MemberCannotConvertedException(ErrorCode.MEMBER_CANNOT_CONVERTED, "멤버 변환 실패");
    }
}
