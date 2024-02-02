package com.task.weaver.domain.storyusertag.entity;

import com.task.weaver.domain.BaseEntity;
import com.task.weaver.domain.story.entity.Story;
import com.task.weaver.domain.user.entity.User;
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

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long storyUserTagId;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "story_id")
        private Story story;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "user_id")
        private User user;
}
