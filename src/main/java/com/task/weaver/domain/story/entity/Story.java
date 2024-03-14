package com.task.weaver.domain.story.entity;

import com.task.weaver.domain.BaseEntity;
import com.task.weaver.domain.comment.entity.Comment;
import com.task.weaver.domain.project.entity.Project;
import com.task.weaver.domain.story.dto.UpdateStory;
import com.task.weaver.domain.task.entity.Task;
import com.task.weaver.domain.user.entity.User;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;
import java.util.List;

@Entity(name = "STORY")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class Story extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "story_id")
    private Long story_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "projectId")
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mention_task_id")
    private Task mentionTask;

    @Column(name = "title")
    private String title;

    @Column(name = "body")
    private String body;

    @Column(name = "created")
    @CreatedDate
    private LocalDate created;

    @Column(name = "due_date")
    private LocalDate dueDate;

    public void update (UpdateStory updateStory) {
        title = updateStory.title();
        body  = updateStory.body();
        mentionTask = updateStory.mention();
    }
}
