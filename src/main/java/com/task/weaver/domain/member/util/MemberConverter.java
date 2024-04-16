package com.task.weaver.domain.member.util;

import com.task.weaver.common.exception.ErrorCode;
import com.task.weaver.common.exception.member.MemberCannotConvertedException;
import com.task.weaver.domain.member.Member;
import com.task.weaver.domain.member.oauth.entity.OauthMember;
import com.task.weaver.domain.member.user.dto.response.ResponseGetMember;
import com.task.weaver.domain.member.user.dto.response.ResponseGetUserForFront;
import com.task.weaver.domain.member.user.entity.User;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import org.springframework.stereotype.Component;

@Component
public class MemberConverter {

    private final Map<Class<? extends Member>, List<Function<Member, ?>>> converters = new HashMap<>();

    public MemberConverter() {
        converters.put(User.class, List.of(this::convertUser, this::convertForFrontByUser));
        converters.put(OauthMember.class, List.of(this::convertOauthMember, this::convertForFrontByOauth));
    }

    public <T> T convert(Member member, Class<T> targetType) {
        List<Function<Member, ?>> converterList = converters.get(member.getClass());
        validConverted(converterList);
        for (Function<Member, ?> converter : converterList) {
            Object result = converter.apply(member);
            if (targetType.isInstance(result)) {
                return targetType.cast(result);
            }
        }
        return null;
    }

    private static void validConverted(final List<Function<Member, ?>> converterList) {
        if (converterList == null) {
            throw new MemberCannotConvertedException(ErrorCode.MEMBER_CANNOT_CONVERTED, "해당 타입으로 변환할 수 없습니다.");
        }
    }

    private ResponseGetMember convertUser(Member member) {
        User user = (User) member;
        return ResponseGetMember.of(user);
    }

    private ResponseGetMember convertOauthMember(Member member) {
        OauthMember oauthMember = (OauthMember) member;
        return ResponseGetMember.of(oauthMember);
    }

    private ResponseGetUserForFront convertForFrontByUser(Member member) {
        return ResponseGetUserForFront.of((User) member);
    }

    private ResponseGetUserForFront convertForFrontByOauth(Member member) {
        return ResponseGetUserForFront.of((OauthMember) member);
    }
}
