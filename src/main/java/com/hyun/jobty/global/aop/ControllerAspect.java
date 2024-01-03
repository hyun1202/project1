package com.hyun.jobty.global.aop;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class ControllerAspect {
    // 포인트컷 표현식
    // 리턴타입 패키지경로..클래스명.메소드명(..)

    // 컨트롤러
    @Pointcut("execution(* com.hyun.jobty.*.controller..*.*(..))")
    private void controllerCut() {}

    @Around("controllerCut()")
    public Object before(ProceedingJoinPoint joinPoint) throws Throwable {
        String cls = joinPoint.getSignature().getDeclaringTypeName();
        //실행되는 함수 이름 읽어옴
        String method = joinPoint.getSignature().getName();

        //메서드 매개변수 배열 읽어옴
        Object[] args = joinPoint.getArgs();
        log.debug("[Controller Aspect]: {} {}()", cls, method);
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        try {
            if (args.length != 0) {
                log.info("[before method] args: " + mapper.enable(SerializationFeature.INDENT_OUTPUT).writeValueAsString(args));
            }
        }catch (Exception e) {
            log.trace("[Controller Aspect]: 파라미터를 읽을 수 없습니다.", e);
        }
        return joinPoint.proceed(args);
    }
}
