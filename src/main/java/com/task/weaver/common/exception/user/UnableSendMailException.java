package com.task.weaver.common.exception.user;

import com.task.weaver.common.exception.BusinessException;
import com.task.weaver.common.exception.ErrorCode;

public class UnableSendMailException extends BusinessException {

    private final boolean checked;

    public UnableSendMailException(Throwable cause, boolean checked) {
        super(ErrorCode.UNABLE_TO_SEND_EMAIL, cause);
        this.checked = checked;
    }

    public UnableSendMailException(ErrorCode errorCode, String message, boolean checked) {
        super(errorCode, message);
        this.checked = checked;
    }

    public boolean isChecked() {
        return checked;
    }
}
