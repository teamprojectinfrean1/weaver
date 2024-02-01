package com.task.weaver.domain.user.repository;

import com.task.weaver.domain.user.entity.User;
import com.task.weaver.domain.user.repository.dsl.UserRepositoryDsl;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryDsl {
    Optional<User> findByName(String nickname);
}
