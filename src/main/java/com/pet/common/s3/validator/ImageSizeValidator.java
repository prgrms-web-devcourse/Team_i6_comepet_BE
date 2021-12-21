package com.pet.common.s3.validator;

import java.util.List;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

public class ImageSizeValidator implements ConstraintValidator<ValidImageSize, List<MultipartFile>> {

    @Override
    public boolean isValid(List<MultipartFile> images, ConstraintValidatorContext context) {
        if (CollectionUtils.isEmpty(images)) {
            return true;
        }
        return images.size() <= 3;
    }

}
