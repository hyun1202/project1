package com.hyun.jobty.global.advice;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class CommAdvice {
    // 포인트컷 표현식
    // 리턴타입 패키지경로..클래스명.메소드명(..)

    // 컨트롤러
    @Pointcut("execution(* com.hyun.jobty.*.controller..*.*(..))")
    private void controllerCut() {}
}
