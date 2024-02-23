package com.task.weaver.domain.websocket.entity;

import com.task.weaver.domain.BaseEntity;
import com.task.weaver.domain.project.entity.Project;
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
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChattingRoom extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Builder.Default
    @OneToMany(mappedBy = "chattingRoom", cascade = CascadeType.ALL)
    private final List<Chatting> chattings = new ArrayList<>();
//    @OneToOne
//    @JoinColumn(name = "user_id")
//    private User user;
    @OneToOne
    @JoinColumn(name = "project_id")
    private Project project;
    public void createChatting(Chatting chatting) {
        chattings.add(chatting);
    }
}
