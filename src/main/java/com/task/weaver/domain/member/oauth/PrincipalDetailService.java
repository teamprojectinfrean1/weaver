package com.task.weaver.domain.member.oauth;

import com.task.weaver.common.exception.ErrorCode;
import com.task.weaver.common.exception.member.UserNotFoundException;
import com.task.weaver.domain.authorization.entity.UserOauthMember;
import com.task.weaver.domain.authorization.repository.UserOauthMemberRepository;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


@Configuration
@AllArgsConstructor
public class PrincipalDetailService implements UserDetailsService {

    private final UserOauthMemberRepository userOauthMemberRepository;

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        UserOauthMember userOauthMember = userOauthMemberRepository.findById(UUID.fromString(username))
                .orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND,
                        ErrorCode.USER_NOT_FOUND.getMessage()));
        return new PrincipalDetails(userOauthMember);
    }
}
