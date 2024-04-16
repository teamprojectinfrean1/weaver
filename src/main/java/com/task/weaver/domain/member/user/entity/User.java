package com.task.weaver.domain.member.user.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.task.weaver.domain.BaseEntity;
import com.task.weaver.domain.member.UserOauthMember;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.net.URL;
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
public class User extends BaseEntity implements UserOauthMember {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)", name = "user_id")
    private UUID userId;

    @Column(length = 30)
    private String id;

    @Column(name = "nickname", length = 20)
    private String nickname;

    @Column(name = "email", length = 100)
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "password")
    private String password;

    @Column(name = "profile_image")
    private URL profileImage;

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
    public URL getProfileImage() {
        return this.profileImage;
    }

    @Override
    public boolean isWeaver() {
        return true;
    }

    @Override
    public UUID getUuid() {
        return this.userId;
    }

    @Override
    public String getNickname() {
        return this.nickname;
    }


}
