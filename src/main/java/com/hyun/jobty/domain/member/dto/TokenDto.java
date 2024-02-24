package com.hyun.jobty.domain.member.dto;

import com.hyun.jobty.domain.member.domain.TokenType;
import lombok.Builder;

public class TokenDto {
    private String id;
    private String accessToken;
    private String refreshToken;
    private TokenType type;

    @Builder
    TokenDto(String id, String accessToken, String refreshToken, TokenType type){
        this.id = id;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.type = type;
    }
}
