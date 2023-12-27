package com.hyun.jobty.member.dto;

import com.hyun.jobty.member.domain.Token;
import lombok.Builder;
import lombok.Getter;

@Getter
public class TokenDto {
    private String accessToken;
    private String refreshToken;

    public TokenDto(String accessToken, String refreshToken){
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    @Builder
    public TokenDto(Token token){
        this.accessToken = token.getAccessToken();
        this.refreshToken = token.getRefreshToken();
    }
    public TokenDto(){}
}
