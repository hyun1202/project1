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
    EmailNotFound(1002, "해당하는 이메일을 찾을 수 없습니다.", "해당하는 이메일을 찾을 수 없습니다."),
    EmailExists(1003, "가입되어 있는 이메일입니다.", "가입되어 있는 이메일입니다."),
    SendConfirmEMail(1004, "해당 아이디로 확인 메일 발송하였습니다. 1분 후 메일을 확인해주세요.", "해당 아이디로 확인 메일 발송하였습니다. 메일을 확인해주세요."),
    AvailableDomain(2000, "사용 가능한 도메인입니다.", "중복된 도메인이 없습니다."),
    DuplicatedDomain(2001, "중복된 도메인입니다. 다른 도메인을 입력해주세요.", "중복된 도메인입니다.")
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
