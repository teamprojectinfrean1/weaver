package com.task.weaver.domain.member.util;

import com.task.weaver.common.exception.ErrorCode;
import com.task.weaver.common.exception.member.MemberCannotConvertedException;
import com.task.weaver.domain.member.Member;
import com.task.weaver.domain.member.oauth.entity.OauthMember;
import org.springframework.core.convert.converter.Converter;

public class MemberToOauthMemberConverter implements Converter<Member, OauthMember> {
    @Override
    public OauthMember convert(final Member source) {
        if (source instanceof OauthMember){
            return (OauthMember) source;
        }
        throw new MemberCannotConvertedException(ErrorCode.MEMBER_CANNOT_CONVERTED, "멤버 변환 실패");
    }
}
