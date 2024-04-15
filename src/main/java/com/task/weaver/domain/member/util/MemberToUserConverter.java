package com.task.weaver.domain.member.util;

import com.task.weaver.common.exception.ErrorCode;
import com.task.weaver.common.exception.member.MemberCannotConvertedException;
import com.task.weaver.domain.member.Member;
import com.task.weaver.domain.member.user.entity.User;
import org.springframework.core.convert.converter.Converter;

public class MemberToUserConverter implements Converter<Member, User> {
    @Override
    public User convert(final Member source) {
        if (source instanceof User){
            return (User) source;
        }
        throw new MemberCannotConvertedException(ErrorCode.MEMBER_CANNOT_CONVERTED, "멤버 변환 실패");
    }
}
