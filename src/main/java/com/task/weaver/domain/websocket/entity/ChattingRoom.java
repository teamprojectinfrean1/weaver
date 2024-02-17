package com.task.weaver.domain.websocket.entity;

import com.task.weaver.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class ChattingRoom {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    @Builder.Default
    @OneToMany(mappedBy = "chattingRoom", cascade = CascadeType.ALL)
    private final List<Chatting> chattings = new ArrayList<>();
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
    public void createChatting(Chatting chatting) {
        chattings.add(chatting);
    }
}
