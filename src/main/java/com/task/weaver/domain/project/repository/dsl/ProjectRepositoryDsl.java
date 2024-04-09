package com.task.weaver.domain.project.repository.dsl;

import com.task.weaver.domain.project.entity.Project;
import com.task.weaver.domain.member.user.entity.User;

import java.util.List;
import java.util.Optional;

public interface ProjectRepositoryDsl {
    Optional<List<Project>> findProjectsByUser(User user);
}
