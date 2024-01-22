package com.task.weaver.domain.story.service.impl;

import com.task.weaver.common.exception.AuthorizationException;
import com.task.weaver.common.exception.NotFoundException;
import com.task.weaver.domain.issue.Issue;
import com.task.weaver.domain.issue.IssueMention;
import com.task.weaver.domain.project.Project;
import com.task.weaver.domain.story.dto.request.RequestCreateStory;
import com.task.weaver.domain.story.entity.Story;
import com.task.weaver.domain.story.service.StoryService;
import com.task.weaver.domain.storyusertag.StoryUserTag;
import com.task.weaver.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/*
    StoryServiceImplDummy - 더미용 StoryService 구현체
    = StoryService 를 사용할 때 관련된 아무런 빈이 없으면 IDE에서 에러를 발생시킴
      방지하고자 더미 빈을 추가
 */
@Service
public class StoryServiceImplDummy implements StoryService {

    @Override
    public Story getStory(Long storyId) throws NotFoundException, AuthorizationException {
        return null;
    }

    @Override
    public Story getStory(Issue issue) throws NotFoundException, AuthorizationException {
        return null;
    }

    @Override
    public Page<Story> getStories(Long projectId, Pageable pageable) throws NotFoundException, AuthorizationException {
        return null;
    }

    @Override
    public Page<Story> getStories(Project project, Pageable pageable) throws NotFoundException, AuthorizationException {
        return null;
    }

    @Override
    public Page<Story> getStories(Project project, User user, Pageable pageable) throws NotFoundException, AuthorizationException {
        return null;
    }

    @Override
    public Page<Story> getStories(Project project, Long userId, Pageable pageable) throws NotFoundException, AuthorizationException {
        return null;
    }

    @Override
    public Page<Story> getStories(Long projectId, Long User, Pageable pageable) throws NotFoundException, AuthorizationException {
        return null;
    }

    @Override
    public Page<Story> getStories(StoryUserTag storyUserTag, Pageable pageable) throws NotFoundException, AuthorizationException {
        return null;
    }

    @Override
    public Story addStory(Story story) throws AuthorizationException {
        return null;
    }

    @Override
    public Story addStory(Story story, IssueMention issueMention) throws AuthorizationException {
        return null;
    }

    @Override
    public Story addStory(RequestCreateStory request) throws AuthorizationException {
        return null;
    }

    @Override
    public void deleteStory(Story story) throws NotFoundException, AuthorizationException {

    }

    @Override
    public void deleteStory(Long storyId) throws NotFoundException, AuthorizationException {

    }

    @Override
    public Story updateStory(Story originalStory, Story newStory) throws NotFoundException, AuthorizationException {
        return null;
    }

    @Override
    public Story updateStory(Long originalStoryId, Story newStory) throws NotFoundException, AuthorizationException {
        return null;
    }
}
