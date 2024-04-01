package com.task.weaver.common.file;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FileNameGenerator {
    private static final String DELIMITER = "_";

    private UUID generateUUID(){
        return UUID.randomUUID();
    }

    public String generateUUID(String fileName) {
        return generateUUID() + DELIMITER + fileName;
    }
}
