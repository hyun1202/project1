package com.hyun.jobty.global.aop;

import com.hyun.jobty.global.annotation.AccountValidator;
import com.hyun.jobty.global.exception.CustomException;
import com.hyun.jobty.global.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class AccountValidatorAspect {
    @Pointcut("@annotation(com.hyun.jobty.global.annotation.AccountValidator)")
    private void accountValidator() {}

    /**
     * 실행 함수의 {@link com.hyun.jobty.global.annotation.AccountValidator} 어노테이션 값에 해당하는 파라미터명의 값을 가져와 토큰 값과 비교
     * @param joinPoint 실행 함수
     * @return 함수 실행
     * @exception CustomException {@link ErrorCode}.IncorrectTokenId
     */
    @Around("accountValidator()")
    public Object validateAccount(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        AccountValidator accountValidator = signature.getMethod().getAnnotation(AccountValidator.class);
        // 파라미터명 확인, 기본값은 id
        String paramName = accountValidator.value();
        Object[] args = joinPoint.getArgs();
        String[] paramNames = signature.getParameterNames();
        // 파라미터명에 해당하는 실제 값을 가져옴
        String id = (String) args[getIndex(paramNames, paramName)];

        log.debug("[validateAccount] id: {}", id);
        // 토큰값과 비교
        UserDetails member = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!member.getUsername().equals(id))
            throw new CustomException(ErrorCode.IncorrectTokenId);
        return joinPoint.proceed(args);
    }

    private int getIndex(String[] names, String name){
        int index = 0;
        for (int i=0; i< names.length; i++){
            index = i;
            if (name.equals(names[i]))
                return index;
        }
        return index;
    }
}
