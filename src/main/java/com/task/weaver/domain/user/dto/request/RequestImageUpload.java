package com.task.weaver.domain.user.dto.request;

public record RequestImageUpload(String url, String metadata, boolean requireSignedURLs) {
}
