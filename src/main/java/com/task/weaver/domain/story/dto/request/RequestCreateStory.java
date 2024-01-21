package com.task.weaver.domain.story.dto.request;

public record RequestCreateStory(Long storyId,
                                 Long writerUserId,
                                 Long mentionTaskId,
                                 String title,
                                 String body) {
}
