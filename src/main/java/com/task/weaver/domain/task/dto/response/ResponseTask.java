package com.task.weaver.domain.task.dto.response;

import java.util.UUID;
import lombok.Getter;

public class ResponseTask {

    @Getter
    public static class SimpleResponse {
        private UUID taskUuid;

        private SimpleResponse(UUID taskUuid) {
            this.taskUuid = taskUuid;
        }

        public static ResponseTask.SimpleResponse of(UUID taskUuid) {
            return new SimpleResponse(taskUuid);
        }
    }
}
