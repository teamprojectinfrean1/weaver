package com.task.weaver.domain.authorization.repository;

import com.task.weaver.domain.member.oauth.entity.OauthMember;
import com.task.weaver.domain.member.user.entity.User;
import com.task.weaver.domain.authorization.entity.UserOauthMember;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserOauthMemberRepository extends JpaRepository<UserOauthMember, UUID> {

    Optional<UserOauthMember> findByUser(User user);

    Optional<UserOauthMember> findByOauthMember(OauthMember oauthMember);
}
