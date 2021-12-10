package com.pet.domains.image.service;

import com.pet.common.s3.service.FileUploadService;
import com.pet.domains.image.domain.Image;
import com.pet.domains.image.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ImageService {

    private final FileUploadService fileUploadService;
    private final ImageRepository imageRepository;

    public Image createImage(MultipartFile imageFile) {
        return imageRepository.save(Image.builder()
            .name(fileUploadService.uploadImage(imageFile))
            .build());
    }

}
