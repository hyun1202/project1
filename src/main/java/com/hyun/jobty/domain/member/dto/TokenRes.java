package com.hyun.jobty.domain.member.dto;

import com.hyun.jobty.domain.member.domain.Token;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class TokenRes {
    @Schema(description = "엑세스 토큰")
    private String accessToken;
    @Schema(description = "리프레시 토큰")
    private String refreshToken;

    @Builder
    public TokenRes(Token token){
        this.accessToken = token.getAccessToken();
        this.refreshToken = token.getRefreshToken();
    }
}
