package com.hyun.jobty.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter

public enum ErrorCode {
    FAIL(-1, "실패했습니다.", "실패했습니다."),
    // 계정 관련
    OperationNotAuthorized(6000, "로그인이 안되어있거나 만료된 토큰입니다.", "만료된 토큰입니다."),
    DuplicatedId(6001, "중복된 아이디입니다.", "중복된 아이디입니다."),
    IncorrectPassword(6004, "아이디 또는 비밀번호가 맞지 않습니다.", "비밀번호가 맞지 않습니다."),
    UserNotFound(6005, "아이디 또는 비밀번호가 맞지 않습니다.", "사용자가 존재하지 않습니다."),
    UnrecognizedRole(6006, "권한이 없습니다.", "권한이 없습니다."),
    AccountExpired(6007, "계정이 만료되었습니다.", "계정이 만료되었습니다."),
    AccountDisabled(6010, "탈퇴한 계정입니다.", "탈퇴한 계정입니다."),
    // 토큰 관련
    TokenExpired(6008, "로그인 시간이 초과되었습니다. 다시 로그인해주세요.", "토큰이 만료되었습니다."),
    UnexpectedToken(6009, "토큰이 맞지 않습니다.", "토큰이 맞지 않습니다."),
    TokenUserNotFound(6005, "토큰에 해당하는 유저가 없습니다.", "토큰에 해당하는 유저가 없습니다."),
    IncorrectTokenId(6011, "비정상적인 접근입니다. 다시 시도해주세요.", "토큰 값과 아이디 값이 맞지 않습니다."),
    // 도메인
    ExistsDomain(7000, "계정에 도메인이 존재합니다.", "이미 계정에 도메인이 있으므로 추가로 생성 불가합니다."),
    DuplicatedDomain(7001, "중복된 도메인입니다. 다른 도메인을 입력해주세요.", "중복된 도메인입니다. 다른 도메인을 입력해주세요."),

    FailedSaveFile(8001, "파일 저장을 저장하지 못했습니다. 다른 파일로 시도해주세요.", "파일 저장에 실패하였습니다.");

    private int code; // 에러 발생 코드
    private String msg; // 응답할 에러 발생 메시지
    private String remark; // 서버에서 확인할 에러 발생 메시지
}
