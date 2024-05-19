package com.task.weaver.domain.member.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import com.task.weaver.common.model.Status;
import com.task.weaver.domain.issue.entity.Issue;
import com.task.weaver.domain.project.entity.Project;
import com.task.weaver.domain.userOauthMember.LoginType;
import com.task.weaver.domain.userOauthMember.UserOauthMember;
import com.task.weaver.domain.userOauthMember.oauth.entity.OauthUser;
import com.task.weaver.domain.userOauthMember.user.entity.User;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MemberTest {

    @InjectMocks
    private Member member;

    @Test
    @DisplayName("멤버 프로젝트 초기 설정 테스트")
    public void testInit_Main_Project() throws Exception {
        Project project = createTestProject();
        member.initMainProject(project);
        assertThat(member.getMainProject().getProjectId()).isEqualTo(project.getProjectId());
    }

    @Test
    @DisplayName("멤버의 메인 프로젝트 변경 테스트")
    public void testUpdate_Main_Project() throws Exception {
        Project project = createTestProject();
        member.updateMainProject(project);
        assertThat(member.getMainProject().getProjectId()).isEqualTo(project.getProjectId());
    }

    @Test
    @DisplayName("멤버 타입 테스트 -> user")
    public void testUserMember_Type() throws Exception {
        // given
        User user = mock(User.class);
        Member mockMember = Member.builder()
                .id(UUID.randomUUID())
                .user(user)
                .loginType(LoginType.WEAVER)
                .build();
        OauthUser oauthUser = mock(OauthUser.class);
        UserOauthMember resolvedMember = mockMember.resolveMemberByLoginType();
        assertThat(resolvedMember).isEqualTo(user);
        assertThat(resolvedMember.isWeaver()).isTrue();
    }

    @Test
    @DisplayName("멤버 타입 테스트 -> oauth")
    public void testOauthMember_Type() throws Exception {
        OauthUser oauthUser = mock(OauthUser.class);
        Member mockMember = Member.builder()
                .id(UUID.randomUUID())
                .oauthMember(oauthUser)
                .loginType(LoginType.OAUTH)
                .build();
        UserOauthMember resolvedMember = mockMember.resolveMemberByLoginType();
        assertThat(resolvedMember).isEqualTo(oauthUser);
        assertThat(resolvedMember.isWeaver()).isFalse();
    }

    @Test
    @DisplayName("멤버 진행 중인 이슈 확인 테스트 -> TODO")
    public void testHasIssue_TODO() throws Exception {
        //GIVEN
        Issue issue = Issue.builder()
                .assignee(member)
                .status(Status.TODO)
                .build();
        //WHEN
        member.getAssigneeIssueList().add(issue);
        Set<Issue> assigneeIssueList = member.getAssigneeIssueList();
        //THEN
        assertThat(!assigneeIssueList.isEmpty()).isTrue();
        assertThat(member.hasAssigneeIssueInProgress()).isFalse();
    }

    @Test
    @DisplayName("멤버 진행 중인 이슈 확인 테스트 -> IN_PROGRESS")
    public void testHasIssue_InProgress() throws Exception {
        //GIVEN
        Issue issue = Issue.builder()
                .assignee(member)
                .status(Status.INPROGRESS)
                .build();
        //WHEN
        member.getAssigneeIssueList().add(issue);
        Set<Issue> assigneeIssueList = member.getAssigneeIssueList();
        //THEN
        assertThat(!assigneeIssueList.isEmpty()).isTrue();
        assertThat(member.hasAssigneeIssueInProgress()).isTrue();
    }

    @Test
    @DisplayName("멤버 진행 중인 이슈 확인 테스트 -> DONE")
    public void testHasIssue_DONE() throws Exception {
        //GIVEN
        Issue issue = Issue.builder()
                .assignee(member)
                .status(Status.DONE)
                .build();
        //WHEN
        member.getAssigneeIssueList().add(issue);
        Set<Issue> assigneeIssueList = member.getAssigneeIssueList();
        //THEN
        assertThat(!assigneeIssueList.isEmpty()).isTrue();
        assertThat(member.hasAssigneeIssueInProgress()).isFalse();
    }

    private Project createTestProject() {
        return Project.builder()
                .projectId(UUID.randomUUID())
                .detail("TEST")
                .name("TEST")
                .endDate(LocalDateTime.now())
                .startDate(LocalDateTime.now())
                .writer(member)
                .modifier(member)
                .build();
    }
}