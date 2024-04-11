package com.task.weaver.domain.member.oauth.entity;

import com.task.weaver.domain.BaseEntity;
import com.task.weaver.domain.chattingRoomMember.ChattingRoomMember;
import com.task.weaver.domain.issue.entity.Issue;
import com.task.weaver.domain.member.LoginType;
import com.task.weaver.domain.project.entity.Project;
import com.task.weaver.domain.projectmember.entity.ProjectMember;
import com.task.weaver.domain.member.Member;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "OAUTH_USER",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "oauth_id_unique",
                        columnNames = {
                                "oauth_server_id",
                                "oauth_server"
                        }
                ),
        }
)
public class OauthMember extends BaseEntity implements Member {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "oauth_member_id")
    private UUID id;

    @Embedded
    private OauthId oauthId;

    @Column(name = "nickname", length = 20)
    private String nickname;

    @Column(name = "profile_image_url")
    private String profileImageUrl;

    @Column(name = "is_online", length = 20)
    private boolean isOnline;

    @Column(name = "login_type")
    @Enumerated(EnumType.STRING)
    private LoginType loginType;

    @OneToMany(mappedBy = "oauthMember", cascade = CascadeType.REMOVE)
    private List<ChattingRoomMember> chattingRoomMemberList;

    @OneToMany(mappedBy = "oauthMember", cascade = CascadeType.REMOVE)
    private List<ProjectMember> projectMemberList;

    @OneToOne
    @JoinColumn(name = "main_project_id")
    private Project mainProject;

    @OneToMany(mappedBy = "OauthCreator", cascade = CascadeType.REMOVE)
    private List<Issue> creatorIssueList = new ArrayList<>();

    @OneToMany(mappedBy = "OauthManager", cascade = CascadeType.REMOVE)
    private List<Issue> managerIssueList = new ArrayList<>();

    public UUID id() {
        return id;
    }

    public OauthId oauthId() {
        return oauthId;
    }

    public String nickname() {
        return nickname;
    }

    public String profileImageUrl() {
        return profileImageUrl;
    }

    public LoginType loginType() {
        return loginType;
    }
}
