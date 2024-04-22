package com.hyun.jobty.domain.member.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TokenType {
    login(60 * 60 * 1000L, true, ""),
    signup(60 * 30 * 1000L, false, "signup"),
    change(60 * 30 * 1000L, false, "account/change/pwd"),
    ;
    private Long exp;
    private boolean refreshTokenRequired;
    private String url;
}
