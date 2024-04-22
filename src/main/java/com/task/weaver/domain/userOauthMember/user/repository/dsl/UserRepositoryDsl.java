package com.task.weaver.domain.userOauthMember.user.repository.dsl;

import com.task.weaver.domain.userOauthMember.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface UserRepositoryDsl {
    Page<User> findUsersForProject(UUID projectId, String nickname, Pageable pageable);
}
