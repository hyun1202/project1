package com.hyun.jobty.advice;

import com.hyun.jobty.global.annotation.AccountValidator;
import com.hyun.jobty.advice.exception.CustomException;
import com.hyun.jobty.advice.exception.ErrorCode;
import com.hyun.jobty.util.Util;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.annotation.ElementType;
import java.lang.reflect.Field;

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
    @Before("accountValidator()")
    public void validateAccount(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        AccountValidator accountValidator = signature.getMethod().getAnnotation(AccountValidator.class);
        // 파라미터명 확인, 기본값은 id
        String paramName = accountValidator.value();
        Object[] args = joinPoint.getArgs();
        String[] paramNames = signature.getParameterNames();
        String id = "-1";
        UserDetails member = null;
        try{
            int index = Util.findIndexArrayValue(paramNames, paramName);
            // type 확인 후 파라미터명에 해당하는 실제 값을 가져옴
            if (accountValidator.type() == ElementType.PARAMETER){
                id = (String) args[index];
            }else if (accountValidator.type() == ElementType.FIELD){
                Field field = ReflectionUtils.findField(args[index].getClass(), accountValidator.field());
                field.setAccessible(true);
                id = (String) ReflectionUtils.getField(field, args[index]);
            }
            member = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        }catch (Exception e){
            throw new CustomException(ErrorCode.ValidateAccountFailed);
        }

        log.debug("[validateAccount] id: {}", id);
        log.debug("[validateAccount] userName: {}", member.getUsername());
        // 토큰값과 비교
        if (!member.getUsername().equals(id))
            throw new CustomException(ErrorCode.IncorrectTokenId);
    }
}
