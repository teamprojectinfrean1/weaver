package com.task.weaver.domain.websocket.repository;

import com.task.weaver.domain.websocket.entity.ChattingRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChattingRoomRepository extends JpaRepository<ChattingRoom, Long> {
}
