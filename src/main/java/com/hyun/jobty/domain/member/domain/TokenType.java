package com.hyun.jobty.domain.member.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TokenType {
    login(60 * 60, true, ""),
    signup(60 * 30, false, "signup"),
    change(60 * 30, false, "account/change/pwd"),
    ;
    private int exp;
    private boolean refreshTokenRequired;
    private String url;
}
