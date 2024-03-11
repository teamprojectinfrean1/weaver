package com.task.weaver.domain.user.repository.dsl;

import com.task.weaver.domain.user.dto.response.ResponseGetUserList;
import com.task.weaver.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepositoryDsl {
    Page<User> findUsersForProject(UUID projectId, String nickname, Pageable pageable);
}
