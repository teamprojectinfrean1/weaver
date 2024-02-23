package com.task.weaver.domain.issue.service;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import com.task.weaver.domain.issue.dto.response.IssueResponse;
import com.task.weaver.domain.issue.entity.Issue;
import com.task.weaver.domain.issue.repository.IssueRepository;
import com.task.weaver.domain.issue.service.IssueService;

// @SpringBootTest -> 모든 빈을 IoC 컨테이너에 등록하고 테스트 진행하기 때문에 테스트 느려짐
@ExtendWith(MockitoExtension.class)
public class IssueServiceTest {
	@Mock
	private IssueRepository issueRepository;

	@InjectMocks
	private IssueService issueService;

	// Issue issue = null;

	@BeforeEach
	void beforeEach() {
		// 테스트 시작할 때 마다 issue 새로 생성
		// id 자동 생성
		// issue = new Issue();
		// issue.
	}

	@Test
	@DisplayName("이슈 불러오기 테스트")
	void getTest() {
		// 이슈 조회할 때, 이슈 없으면 null 반환
		// given
		// Long issueId = 99L;
		// Issue savedIssue = new Issue(99L, Task);

		// stub
		// given(issueRepository.findById(issueId)).willReturn(Optional.of(savedIssue));

		// when
		IssueResponse findIssue = issueService.getIssue(99L);

		// then
		// Assertions.assertThat(findIssue.issueId()).isEqualTo(99L);

	}

}
