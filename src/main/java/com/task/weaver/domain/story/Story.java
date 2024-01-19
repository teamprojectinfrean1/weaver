package com.task.weaver.domain.story;

import com.task.weaver.domain.comment.Comment;
import com.task.weaver.domain.storyusertag.StoryUserTag;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;
import java.util.List;

@Entity(name = "STORY")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Story {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "story_id")
    private Long story_id;

    @OneToMany(mappedBy = "story")
    @Column(name = "comment_list")
    private List<Comment> commentList;

    @OneToMany(mappedBy = "story")
    @Column(name = "story_check_table")
    private List<StoryCheckTable> storyCheckTable;

    /**
     * Todo: User와 매핑
     */

    @Column(name = "title")
    private String title;

    @Column(name = "body")
    private String body;

    @Column(name = "created")
    @CreatedDate
    private LocalDate created;

    @Column(name = "due_date")
    @Nullable
    private LocalDate dueDate;
}
