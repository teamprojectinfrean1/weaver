package com.task.weaver.common.exception.authorization;

import com.task.weaver.common.exception.BusinessException;
import com.task.weaver.common.exception.ErrorCode;

public class InvalidPasswordException extends BusinessException {
	public InvalidPasswordException(Throwable cause) {
		super(ErrorCode.INVALID_PASSWORD, cause);
	}
}
