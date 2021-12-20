package com.pet.common.s3.validator;

import java.util.List;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

public class ImageSizeValidator implements ConstraintValidator<ValidImageSize, List<MultipartFile>> {

    @Override
    public boolean isValid(List<MultipartFile> images, ConstraintValidatorContext context) {
        return images.size() <= 3;
    }

}
