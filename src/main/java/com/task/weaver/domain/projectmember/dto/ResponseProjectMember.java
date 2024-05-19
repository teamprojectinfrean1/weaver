package com.task.weaver.domain.projectmember.dto;

import com.task.weaver.common.model.Permission;
import com.task.weaver.domain.member.entity.Member;
import com.task.weaver.domain.project.entity.Project;
import com.task.weaver.domain.projectmember.entity.ProjectMember;
import java.util.UUID;

public class ResponseProjectMember {

    public static ProjectMember dtoToEntity(Project project, Member member, Permission permission) {
        return ProjectMember.builder()
                .project(project)
                .member(member)
                .permission(permission)
                .build();
    }
}
