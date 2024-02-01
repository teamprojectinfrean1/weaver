package com.task.weaver.domain.story.service.impl;

import com.task.weaver.common.exception.AuthorizationException;
import com.task.weaver.common.exception.NotFoundException;
import com.task.weaver.domain.project.entity.Project;
import com.task.weaver.domain.story.dto.UpdateStory;
import com.task.weaver.domain.story.dto.request.RequestCreateStory;
import com.task.weaver.domain.story.entity.Story;
import com.task.weaver.domain.story.repository.StoryRepository;
import com.task.weaver.domain.story.service.StoryService;
import com.task.weaver.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public Story addStory (RequestCreateStory request) throws AuthorizationException {
        return null;
    }

    @Override
    public void deleteStory (User deleter, Story story) throws NotFoundException, AuthorizationException {
        validateOwner(deleter, story);
        storyRepository.delete(story);
    }

    @Override
    public void deleteStory (User deleter, Long storyId) throws NotFoundException, AuthorizationException {
        Story findStory = getStory(storyId);
        validateOwner(deleter, findStory);
        storyRepository.delete(findStory);
    }

    @Override
    @Transactional
    public Story updateStory (User updater, Story original, UpdateStory updateStory) throws NotFoundException, AuthorizationException {
        validateOwner(updater, original);
        original.update(updateStory);
        return original;
    }

    private void validateOwner (User user, Story story) throws AuthorizationException {
        if (user != story.getUser()) {
            throw new AuthorizationException();
        }
    }
}
