package com.pet.common.s3.validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Target(ElementType.PARAMETER)
@Constraint(validatedBy = ImageSizeValidator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidImageSize {

    String message() default "The input files cannot contain more than 3 images.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
