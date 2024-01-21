package com.task.weaver.domain.story.service;

import com.task.weaver.common.exception.AuthorizationException;
import com.task.weaver.common.exception.NotFoundException;
import com.task.weaver.domain.issue.Issue;
import com.task.weaver.domain.issue.IssueMention;
import com.task.weaver.domain.project.Project;
import com.task.weaver.domain.story.dto.request.RequestCreateStory;
import com.task.weaver.domain.story.entity.Story;
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

    Story addStory (Story story);
    Story addStory (Story story, IssueMention issueMention);
    Story addStory (RequestCreateStory request);

    void deleteStory (Story story);
    void deleteStory (Long storyId);

    Story updateStory (Story originalStory, Story newStory);
    Story updateStory (Long originalStoryId, Story newStory);
}
