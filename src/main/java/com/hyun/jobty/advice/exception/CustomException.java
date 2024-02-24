package com.hyun.jobty.advice.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomException extends RuntimeException{
    private ErrorCode errorCode;

    public CustomException(ErrorCode errorCode){
        super(errorCode.getRemark());
        this.errorCode = errorCode;
        log.info("ErrorCode: {}, ErrorMsg: {}", errorCode.getCode(), errorCode.getMsg());
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
