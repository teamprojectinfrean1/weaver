package com.task.weaver.domain.member.user.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.MediaType;

@Getter
@Builder
public class ResponseImage {

    private MediaType contentType;
    private byte[] content;
}
