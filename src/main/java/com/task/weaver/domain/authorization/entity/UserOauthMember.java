package com.task.weaver.domain.authorization.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.task.weaver.domain.BaseEntity;
import com.task.weaver.domain.chattingRoomMember.ChattingRoomMember;
import com.task.weaver.domain.issue.entity.Issue;
import com.task.weaver.domain.member.LoginType;
import com.task.weaver.domain.member.Member;
import com.task.weaver.domain.member.oauth.entity.OauthMember;
import com.task.weaver.domain.member.user.entity.User;
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
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.provisioning.UserDetailsManager;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_oauth_member", uniqueConstraints = {
        @UniqueConstraint(name = "unique_user_id", columnNames = {"user_id"}),
        @UniqueConstraint(name = "unique_oauth_member_id", columnNames = {"oauth_member_id"})
})
public class UserOauthMember extends BaseEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)", name = "user_oauth_member_id")
    private UUID id;

    @OneToOne
    @JsonBackReference
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne
    @JoinColumn(name = "oauth_id")
    private OauthMember oauthMember;

    @Column(name = "login_type")
    @Enumerated(EnumType.STRING)
    private LoginType loginType;

    @Column(name = "is_online")
    private boolean isOnline;

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<ChattingRoomMember> chattingRoomMemberList;

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<ProjectMember> projectMemberList;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "main_project_id")
    private Project mainProject;

    @OneToMany(mappedBy = "creator", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<Issue> creatorIssueList = new ArrayList<>();

    @OneToMany(mappedBy = "manager", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<Issue> managerIssueList = new ArrayList<>();

    public void updateMainProject(final Project project) {
        this.mainProject = project;
    }

    public Member resolveMemberByLoginType(){
        return loginType.equals(LoginType.OAUTH) ? oauthMember : user;
    }
}
