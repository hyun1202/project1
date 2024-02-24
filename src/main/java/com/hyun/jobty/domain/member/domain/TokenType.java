package com.hyun.jobty.domain.member.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TokenType {
    LOGIN(60 * 60, true),
    SIGNUP(60 * 30, false),
    FINDPW(60 * 30, false),
    ;
    private int exp;
    private boolean refreshTokenRequired;
}
