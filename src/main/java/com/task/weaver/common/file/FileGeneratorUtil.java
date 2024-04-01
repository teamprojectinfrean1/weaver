package com.task.weaver.common.file;


import com.task.weaver.common.config.ImageConfig;
import com.task.weaver.common.exception.ErrorCode;
import com.task.weaver.common.exception.user.StorageException;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.stereotype.Component;

@Component
public class FileGeneratorUtil {

    private static final String URL_FORMAT = "%s/%s";
    private final FileNameGenerator fileNameGenerator;
    private final Path rootPath;

    public FileGeneratorUtil(FileNameGenerator fileNameGenerator, ImageConfig imageConfig) {
        this.fileNameGenerator = fileNameGenerator;
        rootPath = Paths.get(imageConfig.getRootLocation());
    }

    public String generateFileName(String fileName) {
        return fileNameGenerator.generateUUID(fileName);
    }

    public String generateFileUrl(String baseUrl, String fileName) {
        return String.format(URL_FORMAT, baseUrl, fileName);
    }

    public Path generateAbsolutePath(String fileName) {
        Path absolutePath = rootPath.resolve(Paths.get(fileName))
                .normalize()
                .toAbsolutePath();
        if (!absolutePath.getParent().equals(rootPath.toAbsolutePath())) {
            throw new StorageException(ErrorCode.STORE_FILE_OUTSIDE_CURRENT_DIRECTORY_MESSAGE, "잘못된 저장소 위치입니다.");
        }
        return absolutePath;
    }
}
