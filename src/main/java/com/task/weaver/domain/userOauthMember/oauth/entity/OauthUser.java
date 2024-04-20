package com.task.weaver.domain.userOauthMember.oauth.entity;

import com.task.weaver.domain.BaseEntity;
import com.task.weaver.domain.userOauthMember.UserOauthMember;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.net.URL;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Builder
@Getter
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
public class OauthUser extends BaseEntity implements UserOauthMember {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "oauth_member_id")
    private UUID id;

    @Column(name = "member_uuid")
    private UUID memberUuid;

    @Embedded
    private OauthId oauthId;

    @Column(name = "nickname", length = 20)
    private String nickname;

    @Column(name = "profile_image_url")
    private URL profileImageUrl;

    @Override
    public UUID getUuid() {
        return id;
    }

    public OauthId oauthId() {
        return oauthId;
    }

    public String nickname() {
        return nickname;
    }

    @Override
    public URL getProfileImage() {
        return profileImageUrl;
    }

    @Override
    public void updateProfileImage(final URL updatedImageUrlObject) {
        this.profileImageUrl = updatedImageUrlObject;
    }

    @Override
    public void updateMemberUuid(UUID memberUuid) {
        this.memberUuid = memberUuid;
    }

    @Override
    public boolean isWeaver() {
        return false;
    }

    public void updateNickname(final String value) {
        this.nickname = value;
    }
}
