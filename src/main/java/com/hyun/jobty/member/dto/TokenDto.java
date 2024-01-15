package com.hyun.jobty.member.dto;

import com.hyun.jobty.member.domain.Token;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class TokenDto {
    @Schema(description = "엑세스 토큰", example = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJpZCI6InRlc3QxIiwiYXV0aCI6IlJPTEVfVVNFUiIsInN1YiI6InRlc3QxIiwiaWF0IjoxNzA1MzIxMDAwLCJleHAiOjE3MDUzMjgyMDAsImlzcyI6IjAxNTc5NDJAZ21haWwuY29tIn0.YPkG_9tyASOzqsn-ZW_CF8TaFPV8gWwmIZYCtsrJlwSxI3ggydZiSpiL_hs3f84FxHuE15GpwUMMejyLAJ9AMg")
    private String accessToken;
    @Schema(description = "리프레시 토큰", example = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJpZCI6InRlc3QxIiwiYXV0aCI6IlJPTEVfVVNFUiIsInN1YiI6InRlc3QxIiwiaWF0IjoxNzA1MzIxMDAwLCJleHAiOjE3MDUzNjQyMDAsImlzcyI6IjAxNTc5NDJAZ21haWwuY29tIn0.He--gWRN6rkUAjZtvPn6SOd70N8Adr7Z2a6AgZBQulCQmGcGmsjldhUv_erE4DwQpyKRC8oej29VcpWOFIoSSg")
    private String refreshToken;

    @Builder
    public TokenDto(Token token){
        this.accessToken = token.getAccessToken();
        this.refreshToken = token.getRefreshToken();
    }
}
