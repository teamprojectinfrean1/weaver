package com.task.weaver.domain.projecttag;

import com.task.weaver.domain.BaseEntity;
import com.task.weaver.domain.project.Project;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectTag extends BaseEntity {

    @Id
    @Column(name = "project_tag_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long project_tag_id;

    @Column(name = "tag", length = 100, nullable = false)
    private String tag;

    @Column(name = "hex_code", length = 8, nullable = false)
    private String hexCode;
}
