package com.task.weaver.domain.story;

import com.task.weaver.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity(name = "STORY_CHECK_TABLE")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class StoryCheckTable extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "story_check_id")
    private Long story_check_id;

    /**
     * Todo: User와 매핑
     */

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "story")
    private Story story;

    @Column(name = "check_date")
    private LocalDate checkDate;
}
