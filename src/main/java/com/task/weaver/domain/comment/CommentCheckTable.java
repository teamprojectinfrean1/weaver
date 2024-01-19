package com.task.weaver.domain.comment;

import com.task.weaver.domain.BaseEntity;
import com.task.weaver.domain.story.Story;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity(name = "COMMENT_CHECK_TABLE")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class CommentCheckTable extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_check_id")
    private Long comment_check_id;

    /**
     * Todo: User와 매핑
     */

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment")
    private Comment comment;

    @Column(name = "check_date")
    private LocalDate checkDate;
}
