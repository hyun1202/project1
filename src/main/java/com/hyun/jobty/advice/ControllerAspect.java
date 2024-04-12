package com.hyun.jobty.advice;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.hyun.jobty.util.ClientUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j
@Aspect
@Component
public class ControllerAspect {
    // 포인트컷 표현식
    // 리턴타입 패키지경로..클래스명.메소드명(..)

    // 컨트롤러
    @Pointcut("execution(* com.hyun.jobty.*.*.controller..*.*(..))")
    private void controllerCut() {}

    @Before("controllerCut()")
    public void before(JoinPoint joinPoint) throws Throwable {
        //request 가져오기
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        ClientUtils.setRemoteIP(request);

        String cls = joinPoint.getSignature().getDeclaringTypeName();
        //실행되는 함수 이름 읽어옴
        String method = joinPoint.getSignature().getName();

        //메서드 매개변수 배열 읽어옴
        Object[] args = joinPoint.getArgs();
        log.info("[Controller Aspect]: {} {}(), client ip: {}", cls, method, ClientUtils.getIp());
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        try {
            if (args.length != 0) {
                log.info("[before method] args: " + mapper.enable(SerializationFeature.INDENT_OUTPUT).writeValueAsString(args));
            }
        } catch (Exception e) {
            log.trace("[Controller Aspect]: 파라미터를 읽을 수 없습니다.", e);
        }
    }
}
