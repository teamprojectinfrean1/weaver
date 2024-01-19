package com.task.weaver.domain.comment;

import com.task.weaver.domain.story.Story;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;
import java.util.List;

@Entity(name = "COMMENT")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Comment {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long comment_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "story")
    @Column(name = "story")
    private Story story;

    /**
     * Todo: User와 매핑
     */

    @OneToMany(mappedBy = "comment")
    @Column(name = "comment_check_table")
    private List<CommentCheckTable> commentCheckTable;

    private String body;

    @Column(name = "create_date")
    @CreatedDate
    private LocalDate create_date;
}
