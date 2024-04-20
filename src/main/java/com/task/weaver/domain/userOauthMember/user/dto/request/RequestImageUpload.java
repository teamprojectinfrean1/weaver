package com.task.weaver.domain.member.user.dto.request;

public record RequestImageUpload(String url, String metadata, boolean requireSignedURLs) {
}
