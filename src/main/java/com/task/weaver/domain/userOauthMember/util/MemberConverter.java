package com.task.weaver.domain.userOauthMember.util;

import com.task.weaver.common.exception.ErrorCode;
import com.task.weaver.common.exception.member.MemberCannotConvertedException;
import com.task.weaver.domain.userOauthMember.UserOauthMember;
import com.task.weaver.domain.userOauthMember.oauth.entity.OauthUser;
import com.task.weaver.domain.userOauthMember.user.dto.response.ResponseGetMember;
import com.task.weaver.domain.userOauthMember.user.dto.response.ResponseGetUserForFront;
import com.task.weaver.domain.userOauthMember.user.entity.User;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import org.springframework.stereotype.Component;

/**
 * Unused
 */
@Component
public class MemberConverter {

    private final Map<Class<? extends UserOauthMember>, List<Function<UserOauthMember, ?>>> converters = new HashMap<>();

    public MemberConverter() {
        converters.put(User.class, List.of(this::convertUser, this::convertForFrontByUser));
        converters.put(OauthUser.class, List.of(this::convertOauthMember, this::convertForFrontByOauth));
    }

    public <T> T convert(UserOauthMember userOauthMember, Class<T> targetType) {
        List<Function<UserOauthMember, ?>> converterList = converters.get(userOauthMember.getClass());
        validConverted(converterList);
        return converterList.stream()
                .map(converter -> converter.apply(userOauthMember))
                .filter(targetType::isInstance)
                .map(targetType::cast)
                .findFirst()
                .orElseThrow(() ->
                        new MemberCannotConvertedException(ErrorCode.MEMBER_CANNOT_CONVERTED, "해당 타입으로 변환할 수 없습니다."));
    }

    private static void validConverted(final List<Function<UserOauthMember, ?>> converterList) {
        if (converterList == null) {
            throw new MemberCannotConvertedException(ErrorCode.MEMBER_CANNOT_CONVERTED, "해당 타입으로 변환할 수 없습니다.");
        }
    }

    private ResponseGetMember convertUser(UserOauthMember userOauthMember) {
        User user = (User) userOauthMember;
        return ResponseGetMember.of(user, user.getMemberUuid());
    }

    private ResponseGetMember convertOauthMember(UserOauthMember userOauthMember) {
        OauthUser oauthMember = (OauthUser) userOauthMember;
        return ResponseGetMember.of(oauthMember, oauthMember.getMemberUuid());
    }

    private ResponseGetUserForFront convertForFrontByUser(UserOauthMember userOauthMember) {
        return ResponseGetUserForFront.of((User) userOauthMember);
    }

    private ResponseGetUserForFront convertForFrontByOauth(UserOauthMember userOauthMember) {
        return ResponseGetUserForFront.of((OauthUser) userOauthMember);
    }
}
