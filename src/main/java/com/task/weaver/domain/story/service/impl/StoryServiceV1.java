package com.task.weaver.domain.story.service.impl;

import com.task.weaver.common.exception.AuthorizationException;
import com.task.weaver.common.exception.NotFoundException;
import com.task.weaver.domain.issue.entity.IssueMention;
import com.task.weaver.domain.project.entity.Project;
import com.task.weaver.domain.story.dto.request.RequestCreateStory;
import com.task.weaver.domain.story.entity.Story;
import com.task.weaver.domain.story.repository.StoryRepository;
import com.task.weaver.domain.story.service.StoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class StoryServiceV1 implements StoryService {

    private final StoryRepository storyRepository;

    @Override
    public Story getStory (Long storyId) throws NotFoundException, AuthorizationException {
        return storyRepository.findById(storyId).orElseThrow(
                () -> new NotFoundException(""));
    }

    @Override
    public Page<Story> getStories (Project project, Pageable pageable) throws NotFoundException, AuthorizationException {
        return storyRepository.findAllByProject(pageable, project);
    }

    @Override
    public Story addStory(Story story) throws AuthorizationException {
        return storyRepository.save(story);
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
