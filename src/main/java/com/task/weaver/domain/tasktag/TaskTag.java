package com.task.weaver.domain.tasktag;

import com.task.weaver.domain.task.Task;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "TASK_TAG")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class TaskTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_tag_id")
    private Long taskTagId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id")
    private Task task;

    @Column(name = "tag", length = 10)
    private String tag;

    @Column(name = "hex_code", length = 100)
    private String hexCode;

}
