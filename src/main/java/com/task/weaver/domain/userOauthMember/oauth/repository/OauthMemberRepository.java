package com.task.weaver.domain.member.oauth.repository;

import com.task.weaver.domain.member.oauth.entity.OauthId;
import com.task.weaver.domain.member.oauth.entity.OauthUser;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OauthMemberRepository extends JpaRepository<OauthUser, Long> {

    Optional<OauthUser> findByOauthId(OauthId oauthId);
}
