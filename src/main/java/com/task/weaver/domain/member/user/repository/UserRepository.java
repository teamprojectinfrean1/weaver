package com.task.weaver.domain.member.user.repository;

import com.task.weaver.domain.member.user.dto.response.ResponseUserMypage;
import com.task.weaver.domain.member.user.entity.User;
import com.task.weaver.domain.member.user.repository.dsl.UserRepositoryDsl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID>, UserRepositoryDsl {
    Optional<User> findByNickname(String nickname);
    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User as u WHERE u.id = :id")
    Optional<User> findByUserId(@Param("id") String id);

    @Query("SELECT NEW com.task.weaver.domain.user.dto"
            + ".response.ResponseUserMypage(u.nickname, u.email, u.profileImage) FROM User u WHERE u.id = :id")
    Optional<ResponseUserMypage> findUserInfoByUserId(@Param("id") String id);
}
