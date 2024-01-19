package com.task.weaver.domain.project;

import com.task.weaver.domain.BaseEntity;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Project extends BaseEntity {

    @Id
    @Column(name = "project_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long projectId;

    @Column(name = "custom_url", length = 100, nullable = false)
    private String customUrl;

    @Column(name = "banner_url", length = 300, nullable = false)
    private String bannerUrl;

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Column(name = "detail", nullable = false)
    private String detail;

    @Column(name = "due_date", nullable = false)
    private LocalDateTime dueDate;

    @Column(name = "created", nullable = false)
    private LocalDateTime created;

    @Column(name = "is_public", nullable = false)
    private Boolean isPublic;

    /**TODO: 2024-01-19, 금, 23:36  -JEON
    *  TASK: user 테이블 관계 설정 필요
    */
}
