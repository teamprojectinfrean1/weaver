package com.task.weaver.common.config;

import org.springframework.stereotype.Component;

@Component
public class ImageConfig {
    private static final String ROOT_LOCATION ="/Users/ondd/Documents/Java-WorkSpace/weaver-file/generated_files";
    private static final String BASE_URL = "http://localhost:8080/images";

    public String getRootLocation() {
        return ROOT_LOCATION;
    }

    public String getBaseUrl() {
        return BASE_URL;
    }
}
