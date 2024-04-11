package com.task.weaver.domain.projectmember.entity;

import com.task.weaver.domain.BaseEntity;
import com.task.weaver.domain.member.Member;
import com.task.weaver.domain.member.oauth.entity.OauthMember;
import com.task.weaver.domain.project.entity.Project;
import com.task.weaver.domain.member.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Getter
@Builder
@Table(name = "Project_Member")
@NoArgsConstructor
@AllArgsConstructor
public class ProjectMember extends BaseEntity {

    @Id
    @Column(name = "project_member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long projectMemberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
}
