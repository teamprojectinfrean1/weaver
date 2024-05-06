package com.task.weaver.domain.userOauthMember.user.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.task.weaver.domain.BaseEntity;
import com.task.weaver.domain.userOauthMember.UserOauthMember;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.net.URL;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.id.uuid.UuidGenerator;
import org.hibernate.type.SqlTypes;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Table(name = "user")
@Setter
@Builder
public class User extends BaseEntity implements UserOauthMember {

    @Id
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @GeneratedValue(generator = "uuid2")
    @Column(columnDefinition = "BINARY(16)", name = "user_id")
    private UUID userId;

    @Column(name = "member_uuid")
    private UUID memberUuid;

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

    @Override
    public void updatePassword(String updatePassword) {
        this.password = updatePassword;
    }

    @Override
    public void updateNickname(final String nickname) {
        this.nickname = nickname;
    }

    @Override
    public void updateProfileImage(final URL storedFileName) {
        this.profileImage = storedFileName;
    }

    @Override
    public void updateMemberUuid(final UUID memberUuid) {
        this.memberUuid = memberUuid;
    }

    @Override
    public URL getProfileImage() {
        return this.profileImage;
    }

    @Override
    public String getPassword() {
        return this.password;
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
    public UUID getMemberUuid() {
        return memberUuid;
    }

    @Override
    public String getNickname() {
        return this.nickname;
    }
}
