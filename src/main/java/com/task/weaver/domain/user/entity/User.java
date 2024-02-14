package com.task.weaver.domain.user.entity;

import com.task.weaver.domain.user.dto.request.RequestUpdateUser;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "member")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "nickname", length = 20)
    private String name;

    @Column(name = "is_online", length = 20)
    private String isOnline;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "password", length = 20)
    private String password;

    public void updateUser(RequestUpdateUser requestUpdateUser){
        this.name = requestUpdateUser.getNickname();
        this.email = requestUpdateUser.getEmail();
        this.password = requestUpdateUser.getPassword();
    }
}
