package com.task.weaver.domain.user.repository.dsl;

import com.task.weaver.domain.user.dto.response.ResponseGetUserList;
import com.task.weaver.domain.user.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepositoryDsl {
    Optional<List<User>> findUsersForProject(UUID projectId);
}
