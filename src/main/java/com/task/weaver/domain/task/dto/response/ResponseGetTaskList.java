package com.task.weaver.domain.task.dto.response;

import com.task.weaver.domain.task.entity.Task;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseGetTaskList {
    private String taskId;
    private String taskTitle;
    private String taskContent;
    private String taskStatus;

    public ResponseGetTaskList(Task task){
//        this.taskId = task.getTaskId();
        this.taskTitle = task.getTaskTitle();
        this.taskContent = task.getTaskContent();

    }
}
