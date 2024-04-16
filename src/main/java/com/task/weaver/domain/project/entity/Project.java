package com.task.weaver.domain.project.entity;

import com.task.weaver.domain.BaseEntity;
import com.task.weaver.domain.project.dto.request.RequestUpdateProject;
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
    @Column(columnDefinition = "BINARY(16)", name = "project_id")
    private UUID projectId;

//    @Column(name = "custom_url")
//    private String customUrl;

//    @Column(name = "banner_url")
//    private String bannerUrl;

    @Column(name = "name")
    private String name;

    @Column(name = "detail")
    private String detail;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "create_date")
    private LocalDateTime created;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User writer;

    @Builder.Default
    @OneToMany(mappedBy = "project", cascade = CascadeType.REMOVE)
    private List<ProjectMember> projectMemberList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "project", cascade = CascadeType.REMOVE)
    private List<Task> taskList = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "last_update_user_id")
    private User modifier;

    public void changeName(String name) {
        this.name = name;
    }

    public void changeDetail(String detail) {
        this.detail = detail;
    }

    public void updateProject(RequestUpdateProject requestUpdateProject, User updater){
        this.name = requestUpdateProject.projectName();
        this.detail = requestUpdateProject.projectContent();
        this.startDate = requestUpdateProject.startDate();
        this.endDate = requestUpdateProject.endDate();
        this.modifier = updater;
    }

//    public void changePublic() {
//        this.isPublic = !isPublic;
//    }
}
