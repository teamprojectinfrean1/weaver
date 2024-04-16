package com.task.weaver.domain.authorization.repository;

import com.task.weaver.domain.member.oauth.entity.OauthUser;
import com.task.weaver.domain.member.user.entity.User;
import com.task.weaver.domain.authorization.entity.Member;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, UUID>, MemberRepositoryDsl {

    Optional<Member> findByUser(User user);

    Optional<Member> findByOauthMember(OauthUser oauthMember);
}

