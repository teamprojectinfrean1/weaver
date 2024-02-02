package com.task.weaver.domain.story.dto;

import com.task.weaver.domain.task.entity.Task;

public record UpdateStory (String title,
                           String body,
                           Task mention) { }
