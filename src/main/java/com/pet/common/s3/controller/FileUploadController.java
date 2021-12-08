package com.pet.common.s3.controller;

import com.pet.common.s3.service.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RequestMapping("/api/v1/upload")
@RestController
public class FileUploadController {

    private final FileUploadService fileUploadService;

    @PostMapping()
    public String uploadImage(@RequestParam MultipartFile file) {
        return fileUploadService.uploadImage(file);
    }

}
