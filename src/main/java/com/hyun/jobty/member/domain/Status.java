package com.hyun.jobty.member.domain;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Status {
    TEMPORARY("임시 계정"),
    ACTIVATE("계정 활성화"),
    DEACTIVATE("계정 탈퇴");

    private final String remark;
}
