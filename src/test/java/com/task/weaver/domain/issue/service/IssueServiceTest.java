package com.task.weaver.domain.issue.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import com.task.weaver.domain.issue.entity.Issue;
import com.task.weaver.domain.issue.repository.IssueRepository;
import com.task.weaver.domain.issue.service.IssueService;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class IssueServiceTest {
	@Mock
	private IssueRepository issueRepository;

	@InjectMocks
	private IssueService issueService;

	Issue issue = null;

	@BeforeEach
	void beforeEach() {
		// 테스트 시작할 때 마다 issue 새로 생성
		// id 자동 생성
		issue = new Issue();
		// issue.
	}

	@Test
	@DisplayName("이슈 불러오기 테스트")
	void getTest() {
		// issueService.addIssue();
	}

}
