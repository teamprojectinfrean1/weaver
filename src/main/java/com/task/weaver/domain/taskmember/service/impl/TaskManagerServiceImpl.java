package com.task.weaver.domain.taskmember.service.impl;

import com.task.weaver.domain.task.dto.request.RequestUpdateTaskAuthority;
import com.task.weaver.domain.task.dto.response.ResponseTaskAuthority;
import com.task.weaver.domain.task.entity.Task;
import com.task.weaver.domain.taskmember.dto.request.RequestCreateTaskManager;
import com.task.weaver.domain.taskmember.dto.request.RequestUpdateTaskManager;
import com.task.weaver.domain.taskmember.dto.response.ResponseTaskManager;
import com.task.weaver.domain.taskmember.entity.TaskManager;
import com.task.weaver.domain.taskmember.repository.TaskManagerRepository;
import com.task.weaver.domain.taskmember.service.TaskManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskManagerServiceImpl implements TaskManagerService {

    private TaskManagerRepository taskManagerRepository;

    @Override
    public ResponseTaskManager getTaskManager(Long taskManagerId) {
        TaskManager taskManager = taskManagerRepository.findById(taskManagerId).get();

        ResponseTaskManager responseTaskManager = new ResponseTaskManager(taskManager);
        return responseTaskManager;
    }

    @Override
    public Page<TaskManager> getTaskManagers(Task task, Pageable pageable) {
        return taskManagerRepository.findByTask(task, pageable);
    }

    @Override
    public TaskManager addTaskManager(TaskManager taskManager) {
        return taskManagerRepository.save(taskManager);
    }

    @Override
    public ResponseTaskManager addTaskManager(RequestCreateTaskManager request) {
        TaskManager entity = request.toEntity();
        TaskManager save = taskManagerRepository.save(entity);
        ResponseTaskManager responseTaskManager = new ResponseTaskManager(save);
        return responseTaskManager;

    }

    @Override
    public void deleteTaskManager(TaskManager taskManager) {
        taskManagerRepository.delete(taskManager);
    }

    @Override
    public void deleteTaskManager(Long taskManagerId) {
        taskManagerRepository.deleteById(taskManagerId);
    }

    @Override
    public ResponseTaskManager updateTaskManager(TaskManager originalTask, TaskManager newTaskManager) {
        TaskManager taskManager = taskManagerRepository.findById(originalTask.getTaskMangerId()).get();
        taskManager.updateTaskManager(newTaskManager);
        return new ResponseTaskManager(taskManager);
    }

    @Override
    public ResponseTaskManager updateTaskManager(Long originalTaskId, TaskManager newTaskManager) {
        TaskManager taskManager = taskManagerRepository.findById(originalTaskId).get();
        taskManager.updateTaskManager(newTaskManager);
        return new ResponseTaskManager(taskManager);
    }

    @Override
    public ResponseTaskManager updateTaskManager(Long originalTaskId, RequestUpdateTaskManager requestUpdateTaskManager) {
        TaskManager taskManager = taskManagerRepository.findById(originalTaskId).get();
        taskManager.updateTaskManager(requestUpdateTaskManager);

        ResponseTaskManager responseTaskManager = new ResponseTaskManager(taskManager);
        return responseTaskManager;
    }
}
