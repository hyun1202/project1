package com.jobty.global.file.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface FileUpload {
    // path
    String path() default "";
    // memberId 파라미터명
    String idParam() default "id";
    boolean idUsage() default true;
}
