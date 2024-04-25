package com.task.weaver.domain.member.repository;

import com.task.weaver.domain.member.entity.Member;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberRepositoryDsl {

    Page<Member> findMembersByProject(UUID projectId, Pageable pageable);

}
