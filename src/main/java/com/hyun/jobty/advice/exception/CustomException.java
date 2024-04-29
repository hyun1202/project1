package com.hyun.jobty.advice.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomException extends RuntimeException{
    private ErrorCode errorCode;

    public CustomException(ErrorCode errorCode){
        super(errorCode.getRemark());
        this.errorCode = errorCode;
        log.info("ExceptionMethod: \\u001B[36m{}\\u001B[36m", getExceptionMethod());
        log.info("ErrorCode: {}, ErrorMsg: {}", errorCode.getCode(), errorCode.getMsg());
    }
    public String getExceptionMethod(){
        String className = Thread.currentThread().getStackTrace()[3].getClassName();
        String methodName = Thread.currentThread().getStackTrace()[3].getMethodName();
        return className + "." +methodName;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
