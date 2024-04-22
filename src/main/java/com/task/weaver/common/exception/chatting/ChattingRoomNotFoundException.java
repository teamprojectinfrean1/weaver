package com.task.weaver.common.exception.chatting;

import com.task.weaver.common.exception.BusinessException;
import com.task.weaver.common.exception.ErrorCode;

public class ChattingRoomNotFoundException extends BusinessException {

    public ChattingRoomNotFoundException(Throwable cause) {
        super(ErrorCode.CHATTINGROOM_NOT_FOUND, cause);
    }
}
