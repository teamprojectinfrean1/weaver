package com.task.weaver.domain.tasktag;

import com.task.weaver.domain.BaseEntity;
import com.task.weaver.domain.status.entity.Status;
import com.task.weaver.domain.task.entity.Task;
import jakarta.persistence.*;
import jdk.jshell.Snippet;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "TASK_STATUS")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class TaskTag extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_tag_id")
    private Long taskTagId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id")
    private Task task;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id")
    private Status statusTag;

//    @Column(name = "tag", length = 10)
//    private String tag;
//
//    @Column(name = "hex_code", length = 100)
//    private String hexCode;

}
