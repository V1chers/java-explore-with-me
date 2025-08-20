package ru.practicum.ewm.exception.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = NullableNotBlankValidator.class)
public @interface NullableNotBlank {
    String message() default "{поле должно отсутствовать или содержать текст}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
