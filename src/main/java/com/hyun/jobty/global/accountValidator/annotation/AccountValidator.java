package com.hyun.jobty.global.accountValidator.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <pre>
 * 토큰값과 ID(String) 체크
 * 파라미터명 또는 객체명으로 해당하는 값을 찾아 비교한다.
 *
 * <b>value</b>: 비교할 파라미터명 및 객체명, 기본 값은 id
 * <b>type</b>:
 * * [기본값] ElementType.FIELD: Class의 Field에서 찾을 경우 사용
 * * ElementType.PARAMETER: 파라미터에서 찾을 경우 사용
 * <b>field</b>: type이 <u>ElementType.FIELD</u>일 경우사용, 기본 값은 id
 * </pre>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AccountValidator {
    String value() default "req";   // 파라미터명 기본값 id=>req 변경
    ElementType type() default ElementType.FIELD;   // id 기본값 parameter=>field 변경
    String field() default "id";
}
