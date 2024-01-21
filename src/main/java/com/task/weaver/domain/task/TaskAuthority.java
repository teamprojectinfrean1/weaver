package com.task.weaver.domain.task;

import com.task.weaver.domain.project.Project;
import com.task.weaver.domain.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "TASK_AUTHORITY")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class TaskAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_authority_id")
    private Long taskAuthorityId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id")
    private Task task;

    @Column(name = "code", length = 10)
    private String code;

}
