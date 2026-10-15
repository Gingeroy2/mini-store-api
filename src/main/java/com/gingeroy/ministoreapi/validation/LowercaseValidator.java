package com.gingeroy.ministoreapi.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class LowercaseValidator implements ConstraintValidator<Lowercase, String> {
//    这里是我们实现验证逻辑的地方
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null){
            return true;
        }
        return value.equals(value.toLowerCase());
    }
}
