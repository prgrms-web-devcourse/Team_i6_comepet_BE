package com.pet.common.s3.service;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.pet.common.exception.ExceptionMessage;
import com.pet.common.s3.validator.ValidImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
@Component
@Validated
public class FileUploadService {

    private final UploadService uploadService;

    public String uploadImage(@ValidImage MultipartFile file) {
        String fileName = createFileName(file.getOriginalFilename());
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(file.getSize());
        objectMetadata.setContentType(file.getContentType());
        try (InputStream inputStream = file.getInputStream()) {
            uploadService.uploadFile(inputStream, objectMetadata, fileName);
        } catch (IOException e) {
            throw ExceptionMessage.FAIL_CHANGE_IMAGE.getException();
        }
        return uploadService.getFileUrl(fileName);
    }

    private String createFileName(String originalFileName) {
        return UUID.randomUUID().toString().concat(getFileExtension(originalFileName));
    }

    private String getFileExtension(String fileName) {
        try {
            return fileName.substring(fileName.lastIndexOf("."));
        } catch (StringIndexOutOfBoundsException e) {
            throw ExceptionMessage.INVALID_IMAGE_TYPE.getException();
        }
    }

}
