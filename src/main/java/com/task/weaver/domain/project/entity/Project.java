package com.task.weaver.domain.project.entity;

import com.task.weaver.domain.BaseEntity;
import com.task.weaver.domain.user.entity.User;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Project extends BaseEntity {

    @Id
    @Column(name = "project_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long projectId;

    @Column(name = "custom_url")
    private String customUrl;

    @Column(name = "banner_url")
    private String bannerUrl;

    @Column(name = "name")
    private String name;

    @Column(name = "detail")
    private String detail;

    @Column(name = "due_date")
    private LocalDateTime dueDate;

    @Column(name = "created")
    private LocalDateTime created;

    @Column(name = "is_public")
    private Boolean isPublic;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public void changeName(String name) {
        this.name = name;
    }

    public void changeDetail(String detail) {
        this.detail = detail;
    }

    public void changePublic() {
        this.isPublic = !isPublic;
    }
}
