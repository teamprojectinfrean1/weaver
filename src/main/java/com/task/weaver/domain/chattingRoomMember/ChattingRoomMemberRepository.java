package com.task.weaver.domain.chattingRoomMember;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChattingRoomMemberRepository extends JpaRepository<ChattingRoomMember, Long> {
}
