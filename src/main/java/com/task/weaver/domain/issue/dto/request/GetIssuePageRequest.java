package com.task.weaver.domain.issue.dto.request;

import java.util.UUID;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import lombok.Builder;

@Builder
public record GetIssuePageRequest(int page,
								  int size,
								  UUID projectId
							   ) {
	public Pageable getPageable(Sort sort) {
		return PageRequest.of(page - 1, size, sort);
	}
}
