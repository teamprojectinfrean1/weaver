package com.task.weaver.domain.project.entity;

import com.task.weaver.domain.BaseEntity;
import com.task.weaver.domain.member.entity.Member;
import com.task.weaver.domain.project.dto.request.RequestUpdateProject;
import com.task.weaver.domain.projectmember.entity.ProjectMember;
import com.task.weaver.domain.task.entity.Task;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Builder
@Getter
@Setter
@Table(name = "PROJECT")
@NoArgsConstructor
@AllArgsConstructor
public class Project extends BaseEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)", name = "project_id")
    private UUID projectId;

    @Column(name = "name")
    private String name;

    @Column(name = "detail")
    private String detail;

    @Column(name = "project_tag")
    private String tags;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member writer;

    @Builder.Default
    @OneToMany(mappedBy = "project", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private Set<ProjectMember> projectMemberList = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "project", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private Set<Task> taskList = new HashSet<>();

    @Column(name = "project_image")
    private URL projectImage;

    @ManyToOne
    @JoinColumn(name = "last_update_member_id")
    private Member modifier;

    public void changeName(String name) {
        this.name = name;
    }

    public void changeDetail(String detail) {
        this.detail = detail;
    }

    public void updateProject(RequestUpdateProject requestUpdateProject, Member updater) {
        this.name = requestUpdateProject.projectName();
        this.detail = requestUpdateProject.projectContent();
        this.startDate = requestUpdateProject.startDate();
        this.endDate = requestUpdateProject.endDate();
        this.modifier = updater;
    }

    public void updateTag(List<String> projectTags) {
        this.tags = String.join(",", projectTags);
    }
}
