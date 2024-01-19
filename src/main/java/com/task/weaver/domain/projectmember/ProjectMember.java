package com.task.weaver.domain.projectmember;

import com.task.weaver.domain.BaseEntity;
import com.task.weaver.domain.project.Project;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
public class ProjectMember extends BaseEntity {

    @Id
    @Column(name = "project_member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long projectMemberId;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    /**TODO: 2024-01-19, 금, 23:36  -JEON
     *  TASK: user 테이블 관계 설정 필요
     */
}
