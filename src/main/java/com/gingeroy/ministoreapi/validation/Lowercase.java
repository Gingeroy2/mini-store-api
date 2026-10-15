package com.gingeroy.ministoreapi.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD) // 指定目标，通过它我们指定要应用此注解（@interface）的位置
@Retention(RetentionPolicy.RUNTIME) // 指定保留策略，通过它我们指定注解的保留时间
@Constraint(validatedBy = LowercaseValidator.class)

public @interface Lowercase {
//    这里需要写一些样板代码，不需要记忆任何东西
//    —— 标准模板，必须编写以实现自定义注解，但它不包含验证逻辑，验证逻辑位于不同的类中
    String message() default "must be lower case";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
