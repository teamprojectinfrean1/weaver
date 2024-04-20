package com.task.weaver.domain.task.service;

import com.task.weaver.domain.member.user.entity.User;
import com.task.weaver.domain.task.dto.request.RequestCreateTaskAuthority;
import com.task.weaver.domain.task.dto.request.RequestUpdateTaskAuthority;
import com.task.weaver.domain.task.dto.response.ResponseTaskAuthority;
import com.task.weaver.domain.task.entity.Task;
import com.task.weaver.domain.task.entity.TaskAuthority;
import java.awt.print.Pageable;
import org.springframework.data.domain.Page;

public interface TaskAuthorityService {

    ResponseTaskAuthority getTaskAuthority(Long taskAuthorityId);

    Page<TaskAuthority> getTaskAuthorities(Task task, Pageable pageable);

    Page<TaskAuthority> getTaskAuthorities(User user, Pageable pageable);

    TaskAuthority addTaskAuthority(TaskAuthority taskAuthority);

    ResponseTaskAuthority addTaskAuthority(RequestCreateTaskAuthority requestCreateTaskAuthority);

    // 권한이 삭제되면 Task에서 추방
    void deleteTaskAuthority(TaskAuthority taskAuthority);

    void deleteTaskAuthority(Long taskAuthorityId);

    TaskAuthority updateTaskAuthority(TaskAuthority originalTaskAuthority, TaskAuthority newTaskAuthority);

    TaskAuthority updateTaskAuthority(Long originalTaskAuthorityId, TaskAuthority newTaskAuthority);

    ResponseTaskAuthority updateTaskAuthority(Long originalTaskAuthorityId,
                                              RequestUpdateTaskAuthority requestUpdateTaskAuthority);
}