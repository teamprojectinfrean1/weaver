package com.task.weaver.domain.project.entity;

import com.task.weaver.domain.BaseEntity;
import com.task.weaver.domain.projectmember.entity.ProjectMember;
import com.task.weaver.domain.task.entity.Task;
import com.task.weaver.domain.user.entity.User;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Project extends BaseEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    private UUID projectId;
//    @Column(name = "custom_url")
//    private String customUrl;
//    @Column(name = "banner_url")
//    private String bannerUrl;
    @Column(name = "name")
    private String name;
    @Column(name = "detail")
    private String detail;
    @Column(name = "startDate")
    private LocalDateTime startDate;
    @Column(name = "endDate")
    private LocalDateTime endDate;
    @Column(name = "created")
    private LocalDateTime created;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;
    @Builder.Default
    @OneToMany(mappedBy = "project", cascade = CascadeType.REMOVE)
    private List<ProjectMember> projectMemberList = new ArrayList<>();
    @Builder.Default
    @OneToMany(mappedBy = "project", cascade = CascadeType.REMOVE)
    private List<Task> taskList = new ArrayList<>();
    public void changeName(String name) {
        this.name = name;
    }

    public void changeDetail(String detail) {
        this.detail = detail;
    }

//    public void changePublic() {
//        this.isPublic = !isPublic;
//    }
}
