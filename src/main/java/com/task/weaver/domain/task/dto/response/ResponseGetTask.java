package com.task.weaver.domain.task.dto.response;

import com.task.weaver.domain.task.entity.Task;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseGetTask {
    private Long task_id;
    private String title;
    private String content;
    private LocalDateTime start_date;
    private LocalDateTime end_date;

    public ResponseGetTask(Task task){
        this.task_id = task.getTaskId();
        this.title = task.getTitle();
        this.content = task.getContent();
        this.start_date = task.getStart_date();
        this.end_date = task.getEnd_date();
    }
}
