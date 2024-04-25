package com.hyun.jobty.domain.member.dto;

import com.hyun.jobty.domain.member.domain.TokenType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @Builder
@AllArgsConstructor
public class TokenDto {
    private String token_id;
    private String uid;
    private String email;
    private String accessToken;
    private String refreshToken;
    private TokenType type;

    public TokenDto(com.hyun.jobty.domain.member.domain.Token token){
        this.token_id = token.getId();
        this.uid = token.getUid();
        this.accessToken = token.getAccessToken();
        this.refreshToken = token.getRefreshToken();
    }

    public TokenDto(String uid, String email){
        this.uid = uid;
        this.email = email;
    }

    @NoArgsConstructor
    @Getter
    public static class Token {
        @NotNull
        @Schema(description = "엑세스 토큰")
        private String accessToken;
        @NotNull
        @Schema(description = "리프레시 토큰")
        private String refreshToken;

        @Builder
        public Token(TokenDto token){
            this.accessToken = token.getAccessToken();
            this.refreshToken = token.getRefreshToken();
        }
    }
}
