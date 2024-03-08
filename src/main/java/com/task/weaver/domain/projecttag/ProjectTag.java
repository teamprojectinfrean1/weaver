package com.task.weaver.domain.projecttag;

import com.task.weaver.domain.BaseEntity;
import com.task.weaver.domain.project.entity.Project;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
    private Long projectTagId;

    @Column(name = "tag")
    private String tag;

    @Column(name = "hex_code")
    private String hexCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "projectId")
    private Project project;
}
