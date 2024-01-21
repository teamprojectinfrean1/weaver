package com.task.weaver.domain.story.service;

import com.task.weaver.common.exception.AuthorizationException;
import com.task.weaver.common.exception.NotFoundException;
import com.task.weaver.domain.issue.Issue;
import com.task.weaver.domain.project.Project;
import com.task.weaver.domain.story.dto.request.RequestCreateStory;
import com.task.weaver.domain.story.entity.Story;
import com.task.weaver.domain.user.User;
import org.springframework.data.domain.Page;

public interface StoryService {

    Story getStory (Long storyId)
            throws NotFoundException, AuthorizationException;
    Story getStory (Issue issue)
            throws NotFoundException, AuthorizationException;

    Page<Story> getStories (Long projectId)
            throws NotFoundException, AuthorizationException;
    Page<Story> getStories (Project project)
            throws NotFoundException, AuthorizationException;
    Page<Story> getStories (Project project, User user)
            throws NotFoundException, AuthorizationException;
    Page<Story> getStories (Project project, Long userId)
            throws NotFoundException, AuthorizationException;
    Page<Story> getStories (Long projectId, Long User)
            throws NotFoundException, AuthorizationException;

    Story add (Story story);
    Story add (RequestCreateStory request);

}
