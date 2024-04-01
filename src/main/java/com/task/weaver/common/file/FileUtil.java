package com.task.weaver.common.file;

import java.io.File;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FileUtil {

    @Value("${file.base-location}")
    private String baseLocation;

    public File find(final String imageName) {
        String filePath = baseLocation + imageName;
        return new File(filePath);
    }
}
