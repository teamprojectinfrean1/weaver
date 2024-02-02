package com.task.weaver.domain.story.service;

import com.task.weaver.common.exception.AuthorizationException;
import com.task.weaver.common.exception.NotFoundException;
import com.task.weaver.domain.project.entity.Project;
import com.task.weaver.domain.issue.entity.IssueMention;
import com.task.weaver.domain.story.dto.UpdateStory;
import com.task.weaver.domain.story.dto.request.RequestCreateStory;
import com.task.weaver.domain.story.entity.Story;
import com.task.weaver.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StoryService {

    Story getStory (Long storyId)
            throws NotFoundException, AuthorizationException;

    Page<Story> getStories (Project project, Pageable pageable)
            throws NotFoundException, AuthorizationException;

    Story addStory (RequestCreateStory request)
            throws AuthorizationException;

    void deleteStory (User deleter, Story story)
            throws NotFoundException, AuthorizationException;
    void deleteStory (User deleter, Long storyId)
            throws NotFoundException, AuthorizationException;

    Story updateStory (User updater, Story originalStory, UpdateStory updateStory)
            throws NotFoundException, AuthorizationException;
}
