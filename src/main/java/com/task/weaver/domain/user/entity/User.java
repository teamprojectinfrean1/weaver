package com.task.weaver.domain.user.entity;

import java.util.Collection;
import java.util.List;

import com.task.weaver.domain.chattingRoomMember.ChattingRoomMember;
import com.task.weaver.domain.projectmember.ProjectMember;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.task.weaver.domain.user.dto.request.RequestUpdateUser;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "user")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "nickname", length = 20)
    private String name;

    @Column(name = "is_online", length = 20)
    private boolean isOnline;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "password", length = 20)
    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<ChattingRoomMember> chattingRoomMemberList;
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<ProjectMember> projectMemberList;
    public void updateUser(RequestUpdateUser requestUpdateUser){
        this.name = requestUpdateUser.getNickname();
        this.email = requestUpdateUser.getEmail();
        this.password = requestUpdateUser.getPassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getUsername() {
        return this.getEmail();
    }

    /**
     * 계정 만료 여부
     * @return true : 만료 X / false : 만료 O
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 계정 잠금 여부
     * @return true : 잠금 해제 / false : 잠금
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 비밀번호 만료 여부
     * @return true : 만료 X / false : 만료 O
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 사용자 활성화 여부
     * @return true : 활성화 / false : 비활성화
     */
    @Override
    public boolean isEnabled() {
        this.isOnline = true;
        // 이메일 인증 여부를 여기서 확인해줄수도 있음
        return true;
    }
}
