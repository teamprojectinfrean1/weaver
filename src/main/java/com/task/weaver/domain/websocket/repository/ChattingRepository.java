package com.task.weaver.domain.websocket.repository;

import com.task.weaver.domain.websocket.entity.Chatting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChattingRepository extends JpaRepository<Chatting, Long> {

}
