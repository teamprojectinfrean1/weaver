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
    private String nickName;

    @Column(name = "is_online", length = 20)
    private String isOnline;

    @Column(name = "mail", length = 100)
    private String mail;

    @Column(name = "password", length = 20)
    private String password;

    public void updateUser(RequestUpdateUser requestUpdateUser){
        this.nickName = requestUpdateUser.getNickname();
        this.mail = requestUpdateUser.getMail();
        this.password = requestUpdateUser.getPassword();
    }
}
