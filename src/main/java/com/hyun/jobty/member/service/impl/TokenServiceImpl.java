package com.hyun.jobty.member.service.impl;

import com.hyun.jobty.global.exception.CustomException;
import com.hyun.jobty.global.exception.ErrorCode;
import com.hyun.jobty.global.security.jwt.TokenProvider;
import com.hyun.jobty.member.domain.Member;
import com.hyun.jobty.member.domain.Token;
import com.hyun.jobty.member.service.RefreshTokenService;
import com.hyun.jobty.member.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class TokenServiceImpl implements TokenService {
    private final TokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;

    public Token reissueAccessToken(String refreshToken){
        String memberId = tokenProvider.getMemberId(refreshToken);
        if (!validRefreshToken(memberId, refreshToken))
            throw new CustomException(ErrorCode.UnexpectedToken);
        return createAccessToken(memberId);
    }


    private boolean validRefreshToken(String memberId, String refreshToken){
        // 토큰 유효성 검사
        if (!tokenProvider.validToken(refreshToken)){
            return false;
        }

        String strRefreshToken = refreshTokenService.findByMemberId(memberId).getRefreshToken();

        if (!strRefreshToken.equals(refreshToken)){
            return false;
        }
        return true;
    }

    public boolean validAccessToken(String memberId, String accessToken){
        // 토큰 유효성 검사
        if (!tokenProvider.validToken(accessToken)){
            return false;
        }

        String strAccessToken = refreshTokenService.findByMemberId(memberId).getAccessToken();

        if (!strAccessToken.equals(accessToken)){
            return false;
        }
        return true;
    }

    public Token createAccessToken(String memberId){
        Member member = Member.builder()
                .id(memberId)
                .build();
        Token token = getToken(member);
        refreshTokenService.saveToken(member.getId(), token.getAccessToken(), token.getRefreshToken());
        return token;
    }

    /**
     * 토큰을 생성한다
     * @param member 토큰 생성할 member 정보
     * @return {@link Token}
     */
    private Token getToken(Member member){
        String accessToken = tokenProvider.generateToken(member);
        String refreshToken = tokenProvider.generateRefreshToken(member);
        return Token.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
