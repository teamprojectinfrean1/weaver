package com.task.weaver.domain.storyusertag;

import com.task.weaver.domain.BaseEntity;
import com.task.weaver.domain.story.Story;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity(name = "STORY_USER_TAG")
@Builder
public class StoryUserTag extends BaseEntity {

        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long storyUserTagId;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "story")
        private Story story;

        /**
         * Todo: User와 매핑
         */
}
