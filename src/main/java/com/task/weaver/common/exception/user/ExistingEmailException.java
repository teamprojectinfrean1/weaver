package com.task.weaver.common.exception.user;

import com.task.weaver.common.exception.BusinessException;
import com.task.weaver.common.exception.ErrorCode;

public class ExistingEmailException extends BusinessException {
	public ExistingEmailException(Throwable cause) {
		super(ErrorCode.EMAIL_ALREADY_EXISTS, cause);
	}
}
