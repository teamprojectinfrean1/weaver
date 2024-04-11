package com.task.weaver.domain.member;

import com.task.weaver.domain.BaseEntity;
import com.task.weaver.domain.chattingRoomMember.ChattingRoomMember;
import com.task.weaver.domain.issue.entity.Issue;
import com.task.weaver.domain.member.oauth.entity.OauthMember;
import com.task.weaver.domain.member.user.entity.User;
import com.task.weaver.domain.project.entity.Project;
import com.task.weaver.domain.projectmember.entity.ProjectMember;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)", name = "member_id")
    private UUID memberId;

    @OneToOne
    @JoinColumn(name = "weaver_id")
    private User weaver;

    @OneToOne
    @JoinColumn(name = "oauth_id")
    private OauthMember oauth;

    @Column(name = "login_type")
    @Enumerated(EnumType.STRING)
    private LoginType loginType;

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    private List<ChattingRoomMember> chattingRoomMemberList;

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    private List<ProjectMember> projectMemberList;

    @OneToOne
    @JoinColumn(name = "main_project_id")
    private Project mainProject;

    @OneToMany(mappedBy = "creator", cascade = CascadeType.REMOVE)
    private List<Issue> creatorIssueList = new ArrayList<>();

    @OneToMany(mappedBy = "manager", cascade = CascadeType.REMOVE)
    private List<Issue> managerIssueList = new ArrayList<>();
}
