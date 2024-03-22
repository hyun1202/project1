package com.hyun.jobty.domain.member.service;

import com.hyun.jobty.domain.member.domain.Token;
import com.hyun.jobty.domain.member.domain.TokenType;

public interface TokenService {
    Token findByTokenId(String token_id);
    void createTokenIdAndCheckByTokenId(String member_id, TokenType type);
    Token reissueAccessToken(String refreshToken, TokenType type);
    void findTokenIdAndDeleteToken(String member_id, TokenType type);
    Token createToken(String member_id, TokenType type);
    boolean validRefreshToken(String member_id, String refreshToken, TokenType type);
    boolean validJwtToken(String member_id, String accessToken);
    boolean validToken(String token_id, String accessToken, TokenType type);
    void deleteToken(String token_id);
}
