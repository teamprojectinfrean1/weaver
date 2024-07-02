package com.task.weaver.domain.member.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class RequestSignIn {
	@NotEmpty
	private String id;

	@NotEmpty
	private String password;
}
