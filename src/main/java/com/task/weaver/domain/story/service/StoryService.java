package com.task.weaver.domain.story.service;

import com.task.weaver.domain.issue.Issue;
import com.task.weaver.domain.story.entity.Story;
import com.task.weaver.domain.user.User;

import java.util.List;

public interface StoryService {


    Story getStory (Long storyId);
    List<Story> getStory (User user);
    Story getStory (Issue issue);


}
