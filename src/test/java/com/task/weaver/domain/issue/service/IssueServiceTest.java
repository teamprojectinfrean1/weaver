//package com.task.weaver.domain.issue.service;
//
//import static org.assertj.core.api.Assertions.*;
//
//import java.time.LocalDateTime;
//import java.util.Optional;
//
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import com.task.weaver.domain.issue.dto.request.CreateIssueRequest;
//import com.task.weaver.domain.issue.dto.response.IssueResponse;
//import com.task.weaver.domain.issue.entity.Issue;
//import com.task.weaver.domain.issue.repository.IssueRepository;
//import com.task.weaver.domain.issue.service.IssueService;
//import com.task.weaver.domain.project.entity.Project;
//import com.task.weaver.domain.status.entity.StatusTag;
//import com.task.weaver.domain.task.entity.Task;
//import com.task.weaver.domain.task.repository.TaskRepository;
//import com.task.weaver.domain.user.entity.User;
//import com.task.weaver.domain.user.repository.UserRepository;
//
//// @SpringBootTest -> 모든 빈을 IoC 컨테이너에 등록하고 테스트 진행하기 때문에 테스트 느려짐
//// @ExtendWith(MockitoExtension.class)
//// @SpringBootTest
//public class IssueServiceTest {
//	// @Mock
//	// private IssueRepository issueRepository;
//	//
//	// @InjectMocks
//	// private IssueService issueService;
//
//	@Autowired
//	private IssueRepository issueRepository;
//	@Autowired
//	private IssueService issueService;
//	// @Autowired
//	// private TaskRepository taskRepository;
//	// @Autowired
//	// private UserRepository userRepository;
//
//	// Issue issue = null;
//
//	// @Test
//	void addIssue() {
//	// 	// given
//	// 	User user = User.builder()
//	// 		.userId(1L)
//	// 		.name("에림")
//	// 		.isOnline(true)
//	// 		.email("yerim@google.com")
//	// 		.password("qwer1234")
//	// 		.build();
//	//
//	// 	Project project = Project.builder()
//	// 		.projectId(1L)
//	// 		.customUrl("customurl.com")
//	// 		.bannerUrl("bannerurl.com")
//	// 		.name("project name")
//	// 		.detail("project detail")
//	// 		.dueDate(LocalDateTime.now())
//	// 		.created(LocalDateTime.now())
//	// 		.isPublic(true)
//	// 		.user(user)
//	// 		.build();
//	//
//	// 	StatusTag statusTag = StatusTag.builder()
//	// 		.statusTagId(1L)
//	// 		.hexCode("1111111")
//	// 		.build();
//	//
//	// 	Task task = Task.builder()
//	// 		.taskId(1L)
//	// 		.project(project)
//	// 		.statusTag(statusTag)
//	// 		.user(user)
//	// 		.taskName("Task name")
//	// 		.detail("Task detail")
//	// 		.dueDate(LocalDateTime.now())
//	// 		.build();
//	//
//	// 	CreateIssueRequest createIssueRequest = CreateIssueRequest.builder()
//	// 		.userId(user.getUserId())
//	// 		.taskId(task.getTaskId())
//	// 		.statusTagId(statusTag.getStatusTagId())
//	// 		.issueName("Issue Name")
//	// 		.issueText("Issue Text")
//	// 		.issueType("Issue Type")
//	// 		.build();
//
//		// issueService.addIssue(createIssueRequest);
//
//		// IssueResponse res = issueService.getIssue(1L);
//
//		// assertThat(res.issueName()).isEqualTo("Issue Name");
//	}
//
//	// @BeforeEach
//	// void beforeEach() {
//	// 	// 테스트 시작할 때 마다 issue 새로 생성
//	// 	// id 자동 생성
//	// 	// issue = new Issue();
//	// 	// issue.
//	// }
//
//	// @Test
//	// @DisplayName("이슈 불러오기 테스트")
//	// void getTest() {
//	// 	// 이슈 조회할 때, 이슈 없으면 null 반환
//	// 	// given
//	// 	// Long issueId = 99L;
//	// 	// Issue savedIssue = new Issue(99L, Task);
//	//
//	 	// stub
//	// 	// given(issueRepository.findById(issueId)).willReturn(Optional.of(savedIssue));
//	//
//	// 	// when
//	// 	IssueResponse findIssue = issueService.getIssue(99L);
//	//
//	// 	// then
//	// 	// Assertions.assertThat(findIssue.issueId()).isEqualTo(99L);
//	//
//	// }
//
//}
