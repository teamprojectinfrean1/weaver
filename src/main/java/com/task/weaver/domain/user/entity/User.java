package com.task.weaver.domain.user.entity;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.task.weaver.domain.BaseEntity;
import com.task.weaver.domain.chattingRoomMember.ChattingRoomMember;
import com.task.weaver.domain.project.entity.Project;
import com.task.weaver.domain.issue.entity.Issue;
import com.task.weaver.domain.projectmember.entity.ProjectMember;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class User extends BaseEntity implements UserDetails{

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)", name = "user_id")
    private UUID userId;

    @Column(length = 30)
    private String id;

    @Column(name = "nickname", length = 20)
    private String nickname;

    @Column(name = "is_online", length = 20)
    private boolean isOnline;

    @Column(name = "email", length = 100)
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "password")
    private String password;

    @Column(name = "profile_image")
    private URL profileImage;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<ChattingRoomMember> chattingRoomMemberList;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<ProjectMember> projectMemberList;

    @OneToOne
    @JoinColumn(name = "main_project_id")
    private Project mainProject;

    @OneToMany(mappedBy = "creator", cascade = CascadeType.REMOVE)
    private List<Issue> creatorIssueList = new ArrayList<>();

    @OneToMany(mappedBy = "manager", cascade = CascadeType.REMOVE)
    private List<Issue> managerIssueList = new ArrayList<>();

    public void updateEmail(final String email) {
        this.email = email;
    }

    public void updatePassword(String updatePassword) {
        this.password = updatePassword;
    }

    public void updateNickname(final String nickname) {
        this.nickname = nickname;
    }

    public void updateProfileImage(final URL storedFileName) {
        this.profileImage = storedFileName;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getUsername() {
        return this.nickname;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
