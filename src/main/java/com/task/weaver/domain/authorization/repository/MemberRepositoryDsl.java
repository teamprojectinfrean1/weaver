package com.task.weaver.domain.authorization.repository;

import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberRepositoryDsl {
    Page<Object[]> findMembersByProject(UUID projectId, Pageable pageable);
}
