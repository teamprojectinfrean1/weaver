package com.task.weaver.domain.member.user.dto.response;

import lombok.Builder;

@Builder
public class ResponseFileSaveResult {

    private String url;
    private String fileName;
    private String originalFileName;
}
