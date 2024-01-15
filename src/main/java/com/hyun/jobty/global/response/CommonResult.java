package com.hyun.jobty.global.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CommonResult {
    @Schema(description = "성공 여부", example = "true")
    private boolean success;
    @Schema(description = "서버 코드, 성공: 1 실패: -1 or 자체 에러 코드", example = "0")
    private int code;
    @Schema(description = "응답 메시지", example = "성공하였습니다.")
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
