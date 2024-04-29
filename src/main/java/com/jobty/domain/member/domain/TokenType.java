package com.jobty.domain.member.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TokenType {
    // refresh 토큰 만료시간으로 설정.
    signin(60 * 60L * 12, true, ""),
    signup(60 * 30L , false, "signup"),
    change(60 * 30L, false, "account/change/pwd"),
    ;
    private Long exp;
    private boolean refreshTokenRequired;
    private String url;

    public void setLoginExp(Long exp) {
        this.exp = exp;
    }
}
