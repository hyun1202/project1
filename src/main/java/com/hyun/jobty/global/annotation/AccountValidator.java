package com.hyun.jobty.global.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
/**
 * 토큰 값과 ID 체크
 */
public @interface AccountValidator {
    String value() default "id";
}
