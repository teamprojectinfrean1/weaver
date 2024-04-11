package com.task.weaver.domain.member;

import com.task.weaver.domain.member.oauth.entity.OauthMember;
import com.task.weaver.domain.member.user.entity.User;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberRepository extends JpaRepository<Member, UUID> {

//    @Query("SELECT m FROM Member m JOIN m.oauth o WHERE o.oauthId.oauthServerId = :oauthServerId AND o.oauthId.oauthServerType = :oauthServerType")
//    Optional<Member> findByOauthId(@Param(("oauthServerId")) String oauth_id, @Param("oauthServerType") OauthServerType oauthServerType);

    Optional<Member> findByOauth(OauthMember oauthMember);

    @Query("SELECT m FROM Member m JOIN m.weaver u WHERE u.userId = :userId")
    Optional<Member> findByUserId(@Param("userId") UUID userId);

    Optional<Member> findByWeaver(User user);
}
