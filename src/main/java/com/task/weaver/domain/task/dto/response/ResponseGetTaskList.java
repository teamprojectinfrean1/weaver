package com.task.weaver.domain.task.dto.response;

import com.task.weaver.domain.task.entity.Task;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseGetTaskList {
    private UUID taskId;
    private String taskTitle;
    private String taskContent;
    private String taskStatus;

    public ResponseGetTaskList(Task task){
        this.taskId = task.getTaskId();
        this.taskTitle = task.getTaskTitle();
        this.taskContent = task.getTaskContent();
        this.taskStatus = task.getStatus();
    }
}
