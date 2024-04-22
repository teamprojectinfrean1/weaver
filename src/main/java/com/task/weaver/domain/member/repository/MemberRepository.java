package com.task.weaver.domain.member.repository;

import com.task.weaver.domain.userOauthMember.oauth.entity.OauthUser;
import com.task.weaver.domain.userOauthMember.user.entity.User;
import com.task.weaver.domain.member.entity.Member;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, UUID>, MemberRepositoryDsl {

    Optional<Member> findByUser(User user);

    Optional<Member> findByOauthMember(OauthUser oauthMember);
}

