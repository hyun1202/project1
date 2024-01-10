package com.hyun.jobty.global.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CommonResult {
    private boolean success;
    private int code;
    private String msg;

    public CommonResult(){}
    @Builder
    public CommonResult(boolean success, int code, String msg){
        this.success = success;
        this.code = code;
        this.msg = msg;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


}
