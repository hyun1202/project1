package com.jobty.conf.swagger.annotation;

import com.jobty.advice.exception.ErrorCode;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiErrorCode {
    ErrorCode[] value() default {ErrorCode.FAIL};
}
