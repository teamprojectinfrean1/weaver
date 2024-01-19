package com.task.weaver.domain.storyusertag;

import com.task.weaver.domain.story.Story;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity(name = "STORY_USER_TAG")
public class StoryUserTag {

        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long storyUserTagId;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "story")
        private Story story;

        /**
         * Todo: User와 매핑
         */
}
