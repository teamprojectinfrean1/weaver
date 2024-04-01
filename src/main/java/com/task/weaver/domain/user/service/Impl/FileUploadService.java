package com.task.weaver.domain.user.service.Impl;

import com.task.weaver.common.config.ImageConfig;
import com.task.weaver.common.exception.ErrorCode;
import com.task.weaver.common.exception.user.StorageException;
import com.task.weaver.common.file.FileGenerator;
import com.task.weaver.common.file.FileGeneratorUtil;
import com.task.weaver.common.file.FileUtil;
import com.task.weaver.domain.user.dto.response.ResponseFileSaveResult;
import com.task.weaver.domain.user.dto.response.ResponseImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class FileUploadService {

    private static final String PNG = "PNG";
    private final FileUtil fileUtil;

    private final ImageConfig imageConfig;
    private final FileGenerator fileGenerator;
    private final FileGeneratorUtil fileGeneratorUtil;

    public ResponseFileSaveResult store(MultipartFile file) {
        validateFile(file);
        String fileName = fileGeneratorUtil.generateFileName(file.getOriginalFilename()); // 2번
        Path absolutePath = fileGeneratorUtil.generateAbsolutePath(fileName);

        fileGenerator.save(file, absolutePath); // 3번
        return ResponseFileSaveResult.builder()
                .url(fileGeneratorUtil.generateFileUrl(imageConfig.getBaseUrl(), fileName))
                .fileName(fileName)
                .originalFileName(file.getOriginalFilename())
                .build();
    }

    private static void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new StorageException(ErrorCode.EMPTY_FILE, "빈 파일을 저장할 수 없습니다.");
        }
    }

    public ResponseImage load(final String imageName) {
        return ResponseImage.builder()
                .contentType(getContentType(imageName))
                .content(getFileContent(imageName))
                .build();
    }

    private byte[] getFileContent(String imageName) {
        File file = fileUtil.find(imageName);
        try (FileInputStream fis = new FileInputStream(file)) {
            return fis.readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private MediaType getContentType(String imageName) {
        String extension = StringUtils.getFilenameExtension(imageName);
        return Objects.requireNonNull(extension).equalsIgnoreCase(PNG) ? MediaType.IMAGE_PNG : MediaType.IMAGE_JPEG;
    }
}
