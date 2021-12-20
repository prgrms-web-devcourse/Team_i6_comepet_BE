package com.pet.common.s3.validator;

import java.util.Objects;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

public class ImageContentTypeValidator implements ConstraintValidator<ValidImage, MultipartFile> {

    @Override
    public boolean isValid(MultipartFile multipartFile, ConstraintValidatorContext context) {
        boolean result = true;

        String contentType = multipartFile.getContentType();
        if (!isSupportedContentType(Objects.requireNonNull(contentType))) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    "Only PNG or JPG or JPEG or BMP or GIF or TIFF images are allowed.")
                .addConstraintViolation();
            result = false;
        }
        return result;
    }

    private boolean isSupportedContentType(String contentType) {
        return contentType.equals("image/png")
            || contentType.equals("image/jpg")
            || contentType.equals("image/jpeg")
            || contentType.equals("image/bmp")
            || contentType.equals("image/gif")
            || contentType.equals("image/tiff");
    }

}
