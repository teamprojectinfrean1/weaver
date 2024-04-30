package com.task.weaver.domain.member.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.task.weaver.domain.BaseEntity;
import com.task.weaver.domain.chattingRoomMember.ChattingRoomMember;
import com.task.weaver.domain.issue.entity.Issue;
import com.task.weaver.domain.userOauthMember.LoginType;
import com.task.weaver.domain.userOauthMember.UserOauthMember;
import com.task.weaver.domain.userOauthMember.oauth.entity.OauthUser;
import com.task.weaver.domain.userOauthMember.user.entity.User;
import com.task.weaver.domain.project.entity.Project;
import com.task.weaver.domain.projectmember.entity.ProjectMember;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_oauth_member", uniqueConstraints = {
        @UniqueConstraint(name = "unique_user_id", columnNames = {"user_id"}),
        @UniqueConstraint(name = "unique_oauth_member_id", columnNames = {"oauth_member_id"})
})
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)", name = "user_oauth_member_id")
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "oauth_id")
    private OauthUser oauthMember;

    @Column(name = "login_type")
    @Enumerated(EnumType.STRING)
    private LoginType loginType;

    @Column(name = "is_online")
    private boolean isOnline;

    @Builder.Default
    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<ChattingRoomMember> chattingRoomMemberList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<ProjectMember> projectMemberList = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "main_project_id")
    private Project mainProject;

    @Builder.Default
    @OneToMany(mappedBy = "modifier", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private Set<Issue> modifierIssueList = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "assignee", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private Set<Issue> assigneeIssueList = new HashSet<>();

    public void updateMainProject(final Project project) {
        this.mainProject = project;
    }

    public UserOauthMember resolveMemberByLoginType(){
        return loginType.equals(LoginType.OAUTH) ? oauthMember : user;
    }

    public boolean hasAssigneeIssueInProgress(){
        return assigneeIssueList.stream().allMatch(Issue::hasIssueProgress);
    }
}
