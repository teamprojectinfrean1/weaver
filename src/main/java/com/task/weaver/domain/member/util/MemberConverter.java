package com.task.weaver.domain.member.util;

import com.task.weaver.common.exception.ErrorCode;
import com.task.weaver.common.exception.member.MemberCannotConvertedException;
import com.task.weaver.domain.member.Member;
import com.task.weaver.domain.member.oauth.entity.OauthMember;
import com.task.weaver.domain.member.user.dto.response.ResponseGetMember;
import com.task.weaver.domain.member.user.entity.User;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import org.springframework.stereotype.Component;

@Component
public class MemberConverter {

    private final Map<Class<? extends Member>, Function<Member, ResponseGetMember>> converters = new HashMap<>();

    public MemberConverter() {
        converters.put(User.class, this::convertUser);
        converters.put(OauthMember.class, this::convertOauthMember);
    }

    public ResponseGetMember convert(Member member) {
        Function<Member, ResponseGetMember> converter = converters.get(member.getClass());
        if (converter == null) {
            throw new MemberCannotConvertedException(ErrorCode.MEMBER_CANNOT_CONVERTED, "해당 타입으로 변환할 수 없습니다.");
        }
        return converter.apply(member);
    }

    private ResponseGetMember convertUser(Member member) {
        User user = (User) member;
        return ResponseGetMember.of(user);
    }

    private ResponseGetMember convertOauthMember(Member member) {
        OauthMember oauthMember = (OauthMember) member;
        return ResponseGetMember.of(oauthMember);
    }
}
