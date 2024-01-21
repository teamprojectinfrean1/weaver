package com.task.weaver.domain.story.service;

import com.task.weaver.common.exception.AuthorizationException;
import com.task.weaver.common.exception.NotFoundException;
import com.task.weaver.domain.issue.Issue;
import com.task.weaver.domain.issue.IssueMention;
import com.task.weaver.domain.project.Project;
import com.task.weaver.domain.story.dto.request.RequestCreateStory;
import com.task.weaver.domain.story.entity.Story;
import com.task.weaver.domain.storyusertag.StoryUserTag;
import com.task.weaver.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StoryService {

    Story getStory (Long storyId)
            throws NotFoundException, AuthorizationException;
    Story getStory (Issue issue)
            throws NotFoundException, AuthorizationException;

    Page<Story> getStories (Long projectId, Pageable pageable)
            throws NotFoundException, AuthorizationException;
    Page<Story> getStories (Project project, Pageable pageable)
            throws NotFoundException, AuthorizationException;
    Page<Story> getStories (Project project, User user, Pageable pageable)
            throws NotFoundException, AuthorizationException;
    Page<Story> getStories (Project project, Long userId, Pageable pageable)
            throws NotFoundException, AuthorizationException;
    Page<Story> getStories (Long projectId, Long User, Pageable pageable)
            throws NotFoundException, AuthorizationException;
    Page<Story> getStories (StoryUserTag storyUserTag, Pageable pageable)
            throws NotFoundException, AuthorizationException;

    Story addStory (Story story)
            throws AuthorizationException;
    Story addStory (Story story, IssueMention issueMention)
            throws AuthorizationException;
    Story addStory (RequestCreateStory request)
            throws AuthorizationException;

    void deleteStory (Story story)
            throws NotFoundException, AuthorizationException;
    void deleteStory (Long storyId)
            throws NotFoundException, AuthorizationException;

    Story updateStory (Story originalStory, Story newStory)
            throws NotFoundException, AuthorizationException;
    Story updateStory (Long originalStoryId, Story newStory)
            throws NotFoundException, AuthorizationException;
}
