package com.hyun.jobty.global.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CommonCode implements BaseCode{
    SUCCESS(1, "성공하였습니다.", "성공하였습니다."),
    FAIL(-1, "실패하였습니다.", "실패하였습니다."),
    AvailableId(1000, "사용 가능한 아이디입니다.", "중복된 아이디가 없습니다."),
    DuplicatedId(1001, "중복된 아이디입니다.", "중복된 아이디입니다."),
    ;

    private int code;
    private String msg;
    private String remark; // 서버에서 확인할 에러 발생 메시지

    @Override
    public CommonReason getCommonReason() {
        return CommonReason.builder()
                .code(code)
                .msg(msg)
                .remark(remark)
                .name(name())
                .build();
    }
}
